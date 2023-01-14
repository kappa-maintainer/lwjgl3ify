package org.lwjglx.opengl;

public class AMDPerformanceMonitor {
    public static void glBeginPerfMonitorAMD(int monitor) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glBeginPerfMonitorAMD(monitor);
    }

    public static void glDeletePerfMonitorsAMD(int monitor) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glDeletePerfMonitorsAMD(monitor);
    }

    public static void glDeletePerfMonitorsAMD(java.nio.IntBuffer monitors) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glDeletePerfMonitorsAMD(monitors);
    }

    public static void glEndPerfMonitorAMD(int monitor) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glEndPerfMonitorAMD(monitor);
    }

    public static int glGenPerfMonitorsAMD() {
        return org.lwjgl.opengl.AMDPerformanceMonitor.glGenPerfMonitorsAMD();
    }

    public static void glGenPerfMonitorsAMD(java.nio.IntBuffer monitors) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGenPerfMonitorsAMD(monitors);
    }

    public static void glGetPerfMonitorCounterDataAMD(
            int monitor, int pname, java.nio.IntBuffer data, java.nio.IntBuffer bytesWritten) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorCounterDataAMD(monitor, pname, data, bytesWritten);
    }

    public static void glGetPerfMonitorCounterInfoAMD(int group, int counter, int pname, java.nio.ByteBuffer data) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorCounterInfoAMD(group, counter, pname, data);
    }

    public static void glGetPerfMonitorCounterStringAMD(
            int group, int counter, java.nio.IntBuffer length, java.nio.ByteBuffer counterString) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorCounterStringAMD(group, counter, length, counterString);
    }

    public static void glGetPerfMonitorCountersAMD(
            int group,
            java.nio.IntBuffer numCounters,
            java.nio.IntBuffer maxActiveCounters,
            java.nio.IntBuffer counters) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorCountersAMD(
                group, numCounters, maxActiveCounters, counters);
    }

    public static void glGetPerfMonitorGroupStringAMD(
            int group, java.nio.IntBuffer length, java.nio.ByteBuffer groupString) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorGroupStringAMD(group, length, groupString);
    }

    public static void glGetPerfMonitorGroupsAMD(java.nio.IntBuffer numGroups, java.nio.IntBuffer groups) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glGetPerfMonitorGroupsAMD(numGroups, groups);
    }

    public static void glSelectPerfMonitorCountersAMD(int monitor, boolean enable, int group, int counter) {

        org.lwjgl.opengl.AMDPerformanceMonitor.glSelectPerfMonitorCountersAMD(
                monitor, enable, group, new int[] {counter});
    }

    public static void glSelectPerfMonitorCountersAMD(
            int monitor, boolean enable, int group, java.nio.IntBuffer counterList) {
        org.lwjgl.opengl.AMDPerformanceMonitor.glSelectPerfMonitorCountersAMD(monitor, enable, group, counterList);
    }
}