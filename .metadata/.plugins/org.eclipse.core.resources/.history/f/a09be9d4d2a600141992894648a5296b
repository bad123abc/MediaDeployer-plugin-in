package jp.co.dreamarts.plugin.mediadeployer.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsolePanel {
    static MessageConsole console = new MessageConsole("Media Deployer", null);


    public static void getConsole(String s) {
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
        ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
        MessageConsoleStream consoleStream = console.newMessageStream();
        consoleStream.println(s);
    }

}
