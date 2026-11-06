import re

path = r'F:\gjxt\gyxj\pages\monthly\monthly.uvue'
with open(path, 'rb') as f:
    data = f.read()
text = data.decode('utf-8')
lines = text.split('\n')

print(f'Total lines: {len(lines)}')

QUOTE = chr(39)
BACKSLASH = chr(92)

# Find all method definitions
methods = []
for i, line in enumerate(lines, 1):
    stripped = line.strip()
    if re.match(r'^[a-zA-Z_]\w*\s*\([^)]*\)\s*[:\s]', stripped):
        tabs = 0
        for b in line.encode('utf-8'):
            if b == 0x09:
                tabs += 1
            else:
                break
        if tabs <= 3:
            methods.append((i, tabs, stripped[:80]))

print(f'\nTop-level methods: {len(methods)}')
for ln, tabs, name in methods:
    print(f'  L{ln} [{tabs}t]: {name}')

# Track brace balance for entire file
balance = 0
in_template = False
in_line_comment = False
in_block_comment = False
template_depth = 0

issues = []

for i, line in enumerate(lines, 1):
    j = 0
    while j < len(line):
        c = line[j]
        if in_line_comment:
            j += 1
            continue
        if in_block_comment:
            if c == '*' and j+1 < len(line) and line[j+1] == '/':
                in_block_comment = False
                j += 2
                continue
            j += 1
            continue
        if in_template:
            if c == '`':
                in_template = False
                template_depth = 0
                j += 1
                continue
            if c == '$' and j+1 < len(line) and line[j+1] == '{':
                template_depth = 1
                j += 2
                while j < len(line):
                    cc = line[j]
                    if cc == '{':
                        template_depth += 1
                    elif cc == '}':
                        template_depth -= 1
                        if template_depth == 0:
                            j += 1
                            break
                    j += 1
                continue
            j += 1
            continue
        if c == '/' and j+1 < len(line) and line[j+1] == '/':
            in_line_comment = True
            j += 2
            continue
        if c == '/' and j+1 < len(line) and line[j+1] == '*':
            in_block_comment = True
            j += 2
            continue
        if c == '`':
            in_template = True
            j += 1
            continue
        if c == QUOTE or c == '"':
            quote = c
            j += 1
            while j < len(line):
                if line[j] == BACKSLASH:
                    j += 2
                    continue
                if line[j] == quote:
                    j += 1
                    break
                j += 1
            continue
        if c == '{':
            balance += 1
        elif c == '}':
            balance -= 1
            if balance < 0:
                issues.append(f'L{i}: balance went negative at column {j+1}')
        j += 1
    in_line_comment = False

print(f'\nFinal brace balance: {balance}')
if issues:
    print('Issues:')
    for iss in issues:
        print(f'  {iss}')
