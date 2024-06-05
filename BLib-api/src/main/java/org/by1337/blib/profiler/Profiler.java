package org.by1337.blib.profiler;

import net.kyori.adventure.text.Component;
import org.by1337.blib.BLib;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Profiler implements IProfiler {

    private Map<String, MethodInfo> time = new HashMap<>();
    private Map<String, Long> started = new HashMap<>();

    public void start(String name) {
        started.put(name, System.nanoTime());
    }
    public void reset(){
        time = new HashMap<>();
        started = new HashMap<>();
    }

    public void end(String name) {
        Long startTime = started.get(name);
        if (startTime == null) throw new IllegalStateException(name + " is not started!");
        long duration = System.nanoTime() - startTime;
        var info = time.computeIfAbsent(name, k -> new MethodInfo());
        info.time += duration;
        info.cals++;
    }

    public Component report() {
        StringBuilder sb = new StringBuilder("\n");
        List<Map.Entry<String, MethodInfo> > list = new ArrayList<>(time.entrySet());
        list.sort(Comparator.comparingLong(e -> e.getValue().time));
        for (Map.Entry<String, MethodInfo> entry : list) {
            MethodInfo methodInfo = entry.getValue();
            sb.append(entry.getKey()).append(": ").append(methodInfo.getTime()).append(" ms. ").append("cals ").append(methodInfo.getCals()).append(".\n");
        }
        return BLib.getApi().getMessage().componentBuilder(sb.toString());
    }

    private class MethodInfo {
        long time;
        int cals;

        public String getTime() {
            long time = TimeUnit.NANOSECONDS.toMillis(this.time);
            if (time > 20) {
                return "&c" + time + "&r";
            } else if (time > 10) {
                return "&e" + time + "&r";
            } else return "&a" + time + "&r";
        }

        public String getCals() {
            if (cals > 1000) {
                return "&c" + cals + "&r";
            } else if (cals > 500) {
                return "&e" + cals + "&r";
            } else return "&a" + cals + "&r";
        }
    }
}
