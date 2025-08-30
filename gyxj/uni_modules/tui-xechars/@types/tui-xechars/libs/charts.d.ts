import { TuiDrawCharts } from './xcharts';
export declare class TuiCharts {
    chartsMap: any;
    option: UTSJSONObject;
    canvasContext: CanvasContext | null;
    context: CanvasRenderingContext2D | null;
    dpr: number;
    offsetWidth: number;
    offsetHeight: number;
    ins: ComponentPublicInstance | null;
    setSize: boolean;
    baseX: number;
    baseY: number;
    oldTipsName: number;
    canvasRect: DOMRect | null;
    constructor();
    getTouchCurrent(e: UniEvent): number;
    tap(e: UniPointerEvent): void;
    touchStart(e: UniTouchEvent): void;
    touchMove(e: UniTouchEvent): void;
    touchEnd(e: UniTouchEvent): void;
    setInsMethod(e: ComponentPublicInstance, rect: DOMRect): void;
    setContext(context: CanvasContext): void;
    getDrawCharts(name: number): TuiDrawCharts | null;
    add(name: number, option: UTSJSONObject): TuiDrawCharts;
}
