import { TuiChartConfig } from "./config";
import { TuiChartsEvent } from "./chartsEvent";
import { ChartScrollOption } from "./Interface";
import { Animation } from "./util";
export declare class Offset {
    x: number;
    y: number;
}
export declare class ChartCurveControlPoints {
    ctrA: Offset;
    ctrB: Offset;
}
export type ChartRange = {
    minRange: number;
    maxRange: number;
};
export type ChartColumnDataPoints = {
    yAxisPoints: number[];
    xAxisPoints: number[];
    calPoints: ChartOptionsChartDataCalPoints[][];
    eachSpacing: number;
};
export type ChartsCandleDataPoints = {
    yAxisPoints: number[];
    xAxisPoints: number[];
    calPoints: ChartOptionsChartDataCalPoints[][][];
    eachSpacing: number;
};
export declare class ChartMapBounds {
    xMin: number;
    xMax: number;
    yMin: number;
    yMax: number;
}
export declare class ChartOptionsExtraTooltip {
    showBox: boolean;
    showArrow: boolean;
    showCategory: boolean;
    borderWidth: number;
    borderRadius: number;
    borderColor: string;
    borderOpacity: number;
    bgColor: string;
    bgOpacity: number;
    gridType: 'solid' | 'dash';
    dashLength: number;
    gridColor: string;
    boxPadding: number;
    fontSize: number;
    lineHeight: number;
    fontColor: string;
    legendShow: boolean;
    legendShape: 'auto' | 'diamond' | 'circle' | 'triangle' | 'square' | 'rect' | 'line';
    splitLine: boolean;
    horizentalLine: boolean;
    xAxisLabel: boolean;
    yAxisLabel: boolean;
    labelBgColor: string;
    labelBgOpacity: number;
    labelFontColor: string;
}
export declare class ChartOptionsExtraMarkLineData {
    value?: number;
    labelText?: string;
    lineColor: string;
    showLabel: boolean;
    labelAlign: 'left' | 'right' | 'top' | 'bottom';
    labelOffsetX: number;
    labelOffsetY: number;
    labelPadding: number;
    labelFontSize: number;
    labelFontColor: string;
    labelBgColor: string;
    labelBgOpacity: number;
    yAxisIndex?: number;
    y?: number;
}
export declare class ChartOptionsExtraMarkLine {
    type: 'solid' | 'dash';
    dashLength: number;
    data: ChartOptionsExtraMarkLineData[];
}
export declare class ChartOptionsExtraColumn {
    type: 'group' | 'stack' | 'meter';
    width: number;
    seriesGap: number;
    categoryGap: number;
    barBorderCircle: boolean;
    barBorderRadius: number[];
    linearType: 'none' | 'opacity' | 'custom';
    linearOpacity: number;
    customColor: string[];
    colorStop: number;
    meterBorder: number;
    meterFillColor: string;
    activeBgColor: string;
    activeBgOpacity: number;
    activeWidth: number;
    labelPosition: 'outside' | 'insideTop' | 'center' | 'bottom';
}
export declare class ChartOptionsExtraMount {
    type: 'mount' | 'sharp' | 'triangle' | 'bar';
    widthRatio: number;
    borderWidth: number;
    barBorderCircle: boolean;
    barBorderRadius: number[];
    linearType: 'none' | 'opacity' | 'custom';
    linearOpacity: number;
    customColor: string[];
    colorStop: number;
}
export declare class ChartOptionsExtraBar {
    type: 'group' | 'stack';
    width?: number;
    seriesGap: number;
    categoryGap: number;
    barBorderCircle: boolean;
    barBorderRadius?: number[];
    linearType: 'none' | 'opacity' | 'custom';
    linearOpacity: number;
    customColor?: string[];
    colorStop: number;
    activeBgColor: string;
    activeBgOpacity: number;
}
export declare class ChartOptionsExtraLine {
    type: 'straight' | 'curve' | 'step';
    width: number;
    activeType: 'none' | 'hollow' | 'solid';
    linearType: 'none' | 'custom';
    onShadow: boolean;
    animation: 'vertical' | 'horizontal';
}
export declare class ChartOptionsExtraArea {
    type: 'straight' | 'curve' | 'step';
    opacity: number;
    addLine: boolean;
    width: number;
    gradient: boolean;
    activeType: 'none' | 'hollow' | 'solid';
}
export declare class ChartOptionsExtraScatter {
    id?: string;
}
export declare class ChartOptionsExtraBubble {
    border: number;
    opacity: number;
}
export declare class ChartOptionsExtraMixColumn {
    width?: number;
    seriesGap?: number;
    categoryGap?: number;
    barBorderCircle: boolean;
    barBorderRadius?: number[];
    linearType: 'none' | 'opacity' | 'custom';
    linearOpacity: number;
    customColor?: string[];
    colorStop?: number;
}
export declare class ChartOptionsExtraMixArea {
    gradient: boolean;
    opacity: number;
}
export declare class ChartOptionsExtraMixLine {
    width: number;
}
export declare class ChartOptionsExtraMix {
    column?: ChartOptionsExtraMixColumn;
    area?: ChartOptionsExtraMixArea;
    line?: ChartOptionsExtraMixLine;
}
export declare class ChartOptionsExtraPie {
    activeOpacity: number;
    activeRadius: number;
    offsetAngle?: number;
    customRadius?: number;
    labelWidth: number;
    border: boolean;
    borderWidth: number;
    borderColor: string;
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraRing {
    ringWidth: number;
    centerColor: string;
    activeOpacity: number;
    activeRadius: number;
    offsetAngle?: number;
    customRadius?: number;
    labelWidth: number;
    border: boolean;
    borderWidth: number;
    borderColor: string;
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraRose {
    type: 'area' | 'radius';
    minRadius?: number;
    activeOpacity: number;
    activeRadius: number;
    offsetAngle?: number;
    labelWidth: number;
    border: boolean;
    borderWidth: number;
    borderColor: string;
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraRadar {
    gridType: 'radar' | 'circle';
    gridColor: string;
    gridCount: number;
    gridEval: number;
    radius?: number;
    axisLabel: boolean;
    axisLabelTofix: number;
    labelShow: boolean;
    labelColor: string;
    labelPointShow: boolean;
    labelPointRadius: number;
    labelPointColor: string;
    opacity: number;
    border: boolean;
    borderWidth: number;
    max?: number;
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraArcbar {
    type: 'default' | 'circle';
    direction: 'cw' | 'ccw';
    width: number;
    lineCap: 'round' | 'square' | 'butt';
    backgroundColor: string;
    startAngle: number;
    endAngle: number;
    radius?: number;
    gap: number;
    centerX?: number;
    centerY?: number;
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraGaugeSplitLine {
    fixRadius?: number;
    splitNumber: number;
    width: number;
    color: string;
    childNumber: number;
    childWidth: number;
}
export declare class ChartOptionsExtraGaugePointer {
    width: number;
    color: string;
}
export declare class ChartOptionsExtraGauge {
    type: string;
    width: number;
    labelColor: string;
    labelOffset: number;
    startAngle: number;
    endAngle: number;
    startNumber?: number;
    endNumber: number;
    formatter?: any;
    format?: string;
    splitLine?: ChartOptionsExtraGaugeSplitLine;
    pointer?: ChartOptionsExtraGaugePointer;
    oldAngle?: number;
    oldData?: number;
}
export declare class ChartOptionsExtraFunnel {
    type: 'funnel' | 'triangle' | 'pyramid';
    activeOpacity: number;
    activeWidth: number;
    border: boolean;
    borderWidth: number;
    borderColor: string;
    fillOpacity: number;
    minSize: number;
    labelAlign: 'right' | 'left';
    linearType: 'none' | 'custom';
    customColor?: string[];
}
export declare class ChartOptionsExtraWord {
    type: 'normal' | 'vertical';
    autoColors: boolean;
}
export declare class ChartOptionsExtraCandleColor {
    upLine: string;
    upFill: string;
    downLine: string;
    downFill: string;
}
export declare class ChartOptionsExtraCandleAverage {
    show: boolean;
    name?: string[];
    day?: number[];
    color?: string[];
}
export declare class ChartOptionsExtraCandle {
    color: ChartOptionsExtraCandleColor;
    average: ChartOptionsExtraCandleAverage;
}
export declare class ChartOptionsExtraMap {
    border: boolean;
    mercator: boolean;
    borderWidth: number;
    borderColor: string;
    fillOpacity: number;
    active: boolean;
    activeTextColor: string;
    activeBorderColor: string;
    activeFillColor: string;
    activeFillOpacity: number;
}
export declare class ChartOptionsExtra {
    tooltip?: ChartOptionsExtraTooltip;
    markLine?: ChartOptionsExtraMarkLine;
    column?: ChartOptionsExtraColumn;
    mount?: ChartOptionsExtraMount;
    bar?: ChartOptionsExtraBar;
    line?: ChartOptionsExtraLine;
    area?: ChartOptionsExtraArea;
    scatter?: ChartOptionsExtraScatter;
    bubble?: ChartOptionsExtraBubble;
    mix?: ChartOptionsExtraMix;
    pie?: ChartOptionsExtraPie;
    ring?: ChartOptionsExtraRing;
    rose?: ChartOptionsExtraRose;
    radar?: ChartOptionsExtraRadar;
    arcbar?: ChartOptionsExtraArcbar;
    gauge?: ChartOptionsExtraGauge;
    funnel?: ChartOptionsExtraFunnel;
    word?: ChartOptionsExtraWord;
    candle?: ChartOptionsExtraCandle;
    map?: ChartOptionsExtraMap;
}
export declare class ChartOptionsTitle {
    name?: string;
    fontSize: number;
    color: string;
    offsetX?: number;
    offsetY?: number;
}
export declare class ChartOptionsSubTitle {
    name?: string;
    fontSize: number;
    color: string;
    offsetX?: number;
    offsetY?: number;
}
export declare class ChartOptionsLegend {
    show: boolean;
    position: 'bottom' | 'top' | 'left' | 'right';
    float: 'center' | 'left' | 'right' | 'top' | 'bottom';
    padding: number;
    margin: number;
    backgroundColor: string;
    borderColor: string;
    borderWidth?: number;
    fontSize: number;
    fontColor: string;
    lineHeight: number;
    hiddenColor: string;
    itemGap: number;
}
export declare class ChartOptionsYAxisData {
    type?: string;
    position: 'left' | 'right' | 'center';
    disabled: boolean;
    axisLine: boolean;
    axisLineColor: string;
    calibration: boolean;
    fontColor: string;
    fontSize?: number;
    textAlign: 'left' | 'right' | 'center';
    title?: string;
    titleFontSize?: number;
    titleOffsetY?: number;
    titleOffsetX?: number;
    titleFontColor: string;
    min?: number;
    max?: number;
    tofix?: number;
    unit?: string;
    formatter?: any;
    format?: string;
    categories?: string[];
}
export declare class ChartOptionsYAxis {
    disabled: boolean;
    disableGrid: boolean;
    splitNumber: number;
    gridType: 'solid' | 'dash';
    dashLength: number;
    gridColor: string;
    padding: number;
    showTitle: boolean;
    data: ChartOptionsYAxisData[];
    gridSet: string;
    fontColor: string;
    tofix: number;
    unit: string;
    fontSize?: number;
    formatter?: any;
    min?: number;
    max?: number;
}
export declare class ChartOptionsXAxis {
    disabled: boolean;
    axisLine: boolean;
    axisLineColor: string;
    calibration: boolean;
    fontColor: string;
    fontSize: number;
    lineHeight: number;
    marginTop: number;
    rotateLabel: boolean;
    rotateAngle: number;
    labelCount?: number;
    itemCount: number;
    boundaryGap: 'center' | 'justify';
    disableGrid: boolean;
    splitNumber?: number;
    gridColor: string;
    gridType: 'solid' | 'dash';
    dashLength: number;
    gridEval: number;
    scrollShow: boolean;
    scrollAlign: 'left' | 'right';
    scrollColor: string;
    scrollBackgroundColor: string;
    min?: number;
    max?: number;
    title?: string;
    titleFontSize: number;
    titleOffsetY: number;
    titleOffsetX: number;
    titleFontColor: string;
    formatter?: any;
    format?: string;
    scrollPosition?: string;
    data: ChartOptionsYAxisData[];
}
export declare class ChartOptionsSeriesData {
    value?: number;
    color?: string;
    name?: string;
    data?: number;
    labelText?: string;
    labelShow: boolean;
    centerText?: string;
    centerTextSize?: number;
    centerTextColor: string;
    formatter?: any;
    position: ChartOptionsChartDataCalPoints;
    angle: number;
    proportion: number;
    textColor: string;
    textSize?: number;
    _startAngle_: number;
    _endAngle_: number;
}
export declare class ChartOptionsMapSeriesPropertiesParent {
    adcode: number;
}
export declare class ChartOptionsMapSeriesProperties {
    adcode: number;
    name: string;
    center: number[];
    centroid: number[];
    childrenNum: number;
    level: string;
    subFeatureIndex: number;
    acroutes: number[];
    parent: ChartOptionsMapSeriesPropertiesParent;
}
export declare class ChartOptionsMapSeriesGeometry {
    type: string;
    coordinates: number[][][][];
}
export declare class ChartOptionsSeries {
    _data?: number[];
    zeroPoints?: number;
    area?: number[];
    index?: number;
    name?: string;
    legendText?: string;
    data?: ChartOptionsSeriesData[];
    dataArr: number[][];
    show: boolean;
    color?: string;
    fillOpacity: number;
    textColor: string;
    textSize?: number;
    textOffset?: number;
    linearIndex?: number;
    type?: string;
    disableLegend: boolean;
    connectNulls: boolean;
    setShadow?: string[];
    linearColor?: string[][];
    style: 'curve' | 'straight';
    addPoint: boolean;
    lineType: 'solid' | 'dash';
    dashLength: number;
    pointShape: 'diamond' | 'circle' | 'triangle' | 'square' | 'none';
    legendShape?: 'diamond' | 'circle' | 'triangle' | 'square' | 'rect' | 'line' | 'none';
    formatter?: any;
    format?: string;
    rotate: boolean;
    seriesMA?: ChartOptionsSeries[];
    _radius_: number;
    _start_: number;
    _proportion_: number;
    _rose_proportion_: number;
    areav: number[];
    radius: number;
    funnelArea: number[];
    labelShow: boolean;
    labelText?: string;
    centerTextColor: string;
    arc: number;
    text: string;
    properties: ChartOptionsMapSeriesProperties;
    geometry: ChartOptionsMapSeriesGeometry;
    _endAngle_: number;
    _oldAngle_: number;
}
export declare class ChartOptionsChartDataLegendDataAreaXY {
    x: number;
    y: number;
}
export declare class ChartOptionsChartDataLegendDataArea {
    start: ChartOptionsChartDataLegendDataAreaXY;
    end: ChartOptionsChartDataLegendDataAreaXY;
    lineStart: ChartOptionsChartDataLegendDataAreaXY;
    lineEnd: ChartOptionsChartDataLegendDataAreaXY;
    width: number;
    height: number;
    wholeWidth: number;
    wholeHeight: number;
    text: string;
    color: string;
    textColor: string;
    textSize: number;
}
export declare class ChartOptionsChartDataLegendData {
    area: ChartOptionsChartDataLegendDataArea;
    points: ChartOptionsSeries[][];
    widthArr: number[];
    heightArr: number[];
}
export declare class ChartOptionsChartDataYAxisDataYAxisWidth {
    position: string;
    width: number;
}
export declare class ChartOptionsChartDataYAxisData {
    rangesFormat?: string[][];
    ranges?: string[][];
    yAxisWidth?: ChartOptionsChartDataYAxisDataYAxisWidth[];
}
export declare class ChartOptionsChartDataXAxisData {
    xAxisPoints: number[];
    startX?: number;
    endX?: number;
    eachSpacing?: number;
    ranges?: number[];
    angle: number;
    xAxisHeight: number;
    rangesFormat: number[];
}
export declare class ChartOptionsChartDataCategoriesData {
    angle: number;
    xAxisHeight: number;
}
export declare class ChartOptionsChartDataCalPoints {
    color?: string;
    x: number;
    y: number;
    y0: number;
    x0: number;
    r: number;
    t: number;
    width?: number;
    height?: number;
    value: number;
}
export declare class ChartOptionsChartDataToolTiPData {
    center: Offset;
    radius: number;
    series: ChartOptionsSeries[];
    angleList: number[];
    innerRadius: number;
    categories: ChartOptionsSeriesData[];
    totalAngle: number;
}
export declare class ChartOptionsChartDataMapData {
    bounds: ChartMapBounds;
    scale: number;
    xoffset: number;
    yoffset: number;
    mercator: boolean;
}
export declare class ChartOptionsChartData {
    legendData: ChartOptionsChartDataLegendData;
    yAxisData: ChartOptionsChartDataYAxisData;
    xAxisData: ChartOptionsChartDataXAxisData;
    categoriesData: ChartOptionsChartDataCategoriesData;
    xAxisPoints?: number[];
    yAxisPoints?: number[];
    calPoints?: ChartOptionsChartDataCalPoints[][];
    calMapPoints?: ChartOptionsChartDataCalPoints[][][];
    eachSpacing: number;
    pieData?: ChartOptionsChartDataToolTiPData;
    radarData?: ChartOptionsChartDataToolTiPData;
    funnelData?: ChartOptionsChartDataToolTiPData;
    arcbarData?: ChartOptionsChartDataToolTiPData;
    gaugeData?: ChartOptionsChartDataToolTiPData;
    wordCloudData?: ChartOptionsSeries[];
    mapData?: ChartOptionsChartDataMapData;
}
export declare class TextList {
    text: string;
    color: string;
    legendShape: string;
    disableLegend: boolean;
}
export declare class ChartOptionsTooltipOption {
    index?: number;
    offset?: Offset;
    textList?: TextList[];
    formatter?: any;
}
export declare class ChartOptionsTooltip {
    textList: TextList[];
    offset: Offset;
    option: ChartOptionsTooltipOption;
    index?: any;
    group: number[];
}
export declare class ChartOptions {
    type: string;
    context?: CanvasRenderingContext2D;
    canvasContext?: CanvasContext;
    width: number;
    height: number;
    categories?: string[];
    categoriesObj?: ChartOptionsSeriesData[];
    series?: ChartOptionsSeries[];
    _series_?: ChartOptionsSeries[];
    area: number[];
    xAxis: ChartOptionsXAxis;
    yAxis: ChartOptionsYAxis;
    legend: ChartOptionsLegend;
    title: ChartOptionsTitle;
    subtitle: ChartOptionsSubTitle;
    extra: ChartOptionsExtra;
    chartData: ChartOptionsChartData;
    tooltip?: ChartOptionsTooltip;
    showTooltip: boolean;
    canvas2d: boolean;
    pixelRatio: number;
    animation: boolean;
    timing: 'easeOut' | 'easeIn' | 'easeInOut' | 'linear';
    duration: number;
    rotate: boolean;
    rotateLock: boolean;
    background: string;
    color?: string[];
    padding?: number[];
    fontSize: number;
    fontColor: string;
    dataLabel: boolean;
    dataPointShape: boolean;
    dataPointShapeType: 'solid' | 'hollow';
    touchMoveLimit: number;
    enableScroll: boolean;
    enableMarkLine: boolean;
    scrollPosition: 'current' | 'left' | 'right';
    _scrollDistance_?: number;
    _rotate_: boolean;
    updateData: boolean;
    seriesMA?: ChartOptionsSeries[];
    yOffset: number;
    xOffset: number;
    DOMRect: DOMRect | null;
    tapLegend: boolean;
}
export interface TuiChartsInterface {
    opts: ChartOptions;
    context: CanvasRenderingContext2D;
    uevent: TuiChartsEvent;
    config: TuiChartConfig;
    scrollOption: ChartScrollOption;
    animationInstance?: Animation;
    offsetWidth: number;
    offsetHeight: number;
    update(data: UTSJSONObject): void;
    zoom(e?: ChartOptionsXAxis): void;
    dobuleZoom(e: UniTouchEvent): void;
    getLegendDataIndex(e: UniEvent): number;
    touchLegend(e: UniEvent): number;
    scrollStart(e: UniEvent): void;
    scroll(e: UniEvent): void;
    scrollEnd(e: UniEvent): void;
    showToolTip(...args: any[]): number;
    getCurrentDataIndex(e: UniEvent): ChartOptionsTooltip;
}
