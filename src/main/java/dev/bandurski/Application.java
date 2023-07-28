package dev.bandurski;

import dev.bandurski.util.SystemInOutConsole;

public class Application {
    public static void main(String[] args) {
        SystemInOutConsole systemInOutConsole = new SystemInOutConsole();
        ApplicationController controller = new ApplicationController(systemInOutConsole);
        controller.run();
    }
}