import { ChartScrollOption } from "./Interface";
import { TuiChartConfig } from "./config";
import { TuiChartsEvent } from "./chartsEvent";
import { ChartOptions, ChartOptionsTooltip, ChartOptionsXAxis, TuiChartsInterface } from "./type";
import { Animation } from "./util";
export declare class TuiDrawCharts implements TuiChartsInterface {
    opts: ChartOptions;
    context: CanvasRenderingContext2D;
    uevent: TuiChartsEvent;
    config: TuiChartConfig;
    scrollOption: ChartScrollOption;
    animationInstance?: Animation;
    offsetWidth: number;
    offsetHeight: number;
    constructor(option: UTSJSONObject);
    setCoordinates(e: UTSJSONObject): void;
    draw(): void;
    clearTooltip(): void;
    update(data: UTSJSONObject): void;
    zoom(e: ChartOptionsXAxis | null): void;
    dobuleZoom(e: UniTouchEvent): void;
    getLegendDataIndex(e: UniEvent): number;
    touchLegend(e: UniEvent): number;
    scrollStart(e: UniEvent): void;
    scroll(e: UniEvent): void;
    scrollEnd(e: UniEvent): void;
    showToolTip(...args: any[]): number;
    getCurrentDataIndex(e: UniEvent): ChartOptionsTooltip;
}
