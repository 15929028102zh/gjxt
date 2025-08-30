export type ChartsEvent = () => void;
export type TouchPosition = {
    x: number;
    y: number;
};
export declare class ChartScrollOption {
    currentOffset: number;
    startTouchX: number;
    distance: number;
    lastMoveTime: number;
    position: string;
    moveCount: number;
}
export type ChartAnimationOption = {
    canvasContext: CanvasContext;
    timing: string;
    duration: number;
    onProcess: (process: number) => void;
    onAnimationFinish: () => void;
};
