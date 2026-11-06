// Timer.ts
type TimerTask = {
	name: symbol | string;
	callback: () => void;
	time: number;
	t?: number;
}

class Timer {
	private timerList: TimerTask[] = [];

	// 添加计时器
	addTimer(name: symbol | string, callback: () => void, time: number = 1000): void {
		const task = this.timerList.find((item) => item.name === name);
		if (task) {
			task.callback = callback;
			task.time = time;
		} else {
			this.timerList.push({ name, callback, time });
		}
		this.runTimer(name);
	}

	// 运行计时器
	private runTimer(name : symbol | string) : void {
		const _this = this;
		(function inner() {
			const task = _this.timerList.find((item) => item.name === name);
			if (!task) return;
			task.t = setTimeout(() => {
				task.callback();
				if (task.t != null) {
					clearTimeout(task.t);
				}
				inner();
			}, task.time) as number;
}

export default new Timer();
	}

	// 清除计时器
	clearTimer(name : symbol | string) : void {
		const taskIndex = this.timerList.findIndex((item) => item.name === name);
		if (taskIndex !== -1) {
			clearTimeout(this.timerList[taskIndex].t);
			this.timerList.splice(taskIndex, 1);
		}
	}
})();