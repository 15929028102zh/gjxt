#!/usr/bin/env python3
"""
按日期拆分 nginx error.log。
默认拆分 F:\\error.log,输出到 F:\\nginx-error-archive\\nginx-error.YYYY-MM-DD.log。

用法:
    python split_nginx_log.py                          # 拆当前 F:\\error.log
    python split_nginx_log.py --src <path>             # 拆指定文件
    python split_nginx_log.py --out <dir>              # 指定输出目录
    python split_nginx_log.py --mode archive           # 拆完后把源文件改名 .processed,避免重复拆
    python split_nginx_log.py --mode watch             # 监控模式:增量归档追加内容(以后跑)
"""
import argparse
import os
import re
import sys
import time
from collections import defaultdict

DEFAULT_SRC = r'F:\error.log'
DEFAULT_OUT = r'F:\nginx-error-archive'
DATE_RE = re.compile(rb'^(\d{4})/(\d{2})/(\d{2})')


def split_full(src, out_dir):
    os.makedirs(out_dir, exist_ok=True)
    handles = {}
    counts = defaultdict(int)
    sizes = defaultdict(int)
    unknown = 0
    total = 0

    with open(src, 'rb') as f:
        for line in f:
            total += 1
            m = DATE_RE.match(line)
            if m:
                date_str = f'{m.group(1).decode()}-{m.group(2).decode()}-{m.group(3).decode()}'
            else:
                date_str = 'unknown'
                unknown += 1

            if date_str not in handles:
                path = os.path.join(out_dir, f'nginx-error.{date_str}.log')
                handles[date_str] = open(path, 'ab')
            handles[date_str].write(line)
            counts[date_str] += 1
            sizes[date_str] += len(line)

    for fh in handles.values():
        fh.close()

    print(f'拆分完成: {total} 行 → {len(counts)} 个文件 → {out_dir}')
    if unknown:
        print(f'  ⚠ 无日期前缀行: {unknown} 行 → nginx-error.unknown.log')
    for d in sorted(counts.keys()):
        mb = sizes[d] / 1024 / 1024
        print(f'  {d}: {counts[d]:>10} 行  {mb:>8.1f} MB')


def watch(src, out_dir, interval=30):
    """监控模式:每 N 秒检查 src 文件大小,只处理新增部分,按日期追加到归档。"""
    os.makedirs(out_dir, exist_ok=True)
    print(f'watch 模式: 监控 {src} → {out_dir},间隔 {interval}s,Ctrl+C 停止')
    pos = os.path.getsize(src)
    pending = b''
    date_re = DATE_RE

    while True:
        time.sleep(interval)
        size = os.path.getsize(src)
        if size < pos:
            pos = 0
        if size == pos:
            continue
        with open(src, 'rb') as f:
            f.seek(pos)
            chunk = f.read(size - pos)
        pos = size
        pending += chunk
        lines = pending.split(b'\n')
        pending = lines[-1] if not pending.endswith(b'\n') else b''
        for line in lines[:-1]:
            if not line:
                continue
            m = date_re.match(line)
            date_str = f'{m.group(1).decode()}-{m.group(2).decode()}-{m.group(3).decode()}' if m else 'unknown'
            path = os.path.join(out_dir, f'nginx-error.{date_str}.log')
            with open(path, 'ab') as fh:
                fh.write(line + b'\n')
        print(f'  [{time.strftime("%H:%M:%S")}] 已处理到 offset {pos} ({len(chunk)} bytes)')


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--src', default=DEFAULT_SRC)
    ap.add_argument('--out', default=DEFAULT_OUT)
    ap.add_argument('--mode', choices=['full', 'archive', 'watch'], default='full',
                    help='full=直接拆;archive=拆完把源改名;watch=持续监控')
    ap.add_argument('--interval', type=int, default=30, help='watch 模式间隔秒数')
    args = ap.parse_args()

    if not os.path.exists(args.src):
        print(f'源文件不存在: {args.src}', file=sys.stderr)
        sys.exit(1)

    if args.mode == 'watch':
        watch(args.src, args.out, args.interval)
        return

    split_full(args.src, args.out)

    if args.mode == 'archive':
        new_path = args.src + '.processed.' + time.strftime('%Y%m%d-%H%M%S')
        os.rename(args.src, new_path)
        print(f'源文件已改名: {new_path}')


if __name__ == '__main__':
    main()