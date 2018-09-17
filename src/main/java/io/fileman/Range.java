package io.fileman;

/**
 * 文件内容范围
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class Range {
    /**
     * 内容计量单位
     */
    private final String unit;
    /**
     * 内容起始下标
     */
    private final long start;
    /**
     * 内容结束下标
     */
    private final long end;

    public Range(String unit, long start, long end) {
        if (unit == null) throw new NullPointerException();
        if (start < 0 || end < 0) throw new IllegalArgumentException("index must not be negative");
        if (start > end && end > 0) throw new IllegalArgumentException("start index must not bigger than end index");
        this.unit = unit;
        this.start = start;
        this.end = end;
    }

    public static Range valueOf(String value) {
        if (value == null) throw new NullPointerException();
        int index = value.indexOf("=");
        if (index < 0) {
            throw new IllegalArgumentException("could not parse: " + value + " to range, expecting pattern like: UNIT=START-END");
        }
        String unit = value.substring(0, index);
        String range = value.substring(index + 1);
        int idx = range.indexOf("-");
        if (idx < 0) {
            throw new IllegalArgumentException("could not parse: " + value + " to range, expecting pattern like: UNIT=START-END");
        }
        String first = range.substring(0, idx);
        String last = range.substring(idx + 1);
        long start = first.isEmpty() ? 0L : Long.valueOf(first);
        long end = last.isEmpty() ? 0L : Long.valueOf(last);
        return new Range(unit, start, end);
    }

    public String getUnit() {
        return unit;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return unit + "=" + (start > 0 ? start : "") + "-" + (end > 0 ? end : "");
    }
}
