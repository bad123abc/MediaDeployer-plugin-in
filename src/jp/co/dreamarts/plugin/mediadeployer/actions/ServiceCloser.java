package jp.co.dreamarts.plugin.mediadeployer.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.IOException;
import java.lang.reflect.Field;

public class ServiceCloser implements IObjectActionDelegate {

    @Override
    public void run(IAction arg0) {
        int processPid;
        if (MediaDeployer.getStartProcess() != null) {
            processPid = ProcessUtil.getPid((MediaDeployer.getStartProcess()));
            try {
                if (com.sun.jna.Platform.isWindows()) {
                    Runtime.getRuntime().exec("cmd /c " + "taskkill /T /F /PID " + processPid + "\n");
                    MediaDeployer.getConsole("SmartDB Hot Deployer has been closed");
                } else if (com.sun.jna.Platform.isMac()) {
                    Runtime.getRuntime().exec("taskkill /T /F /PID " + processPid + "\n");
                    MediaDeployer.getConsole("SmartDB Hot Deployer has been closed");
                }
            } catch (IOException e) {
                MediaDeployer.getConsole(e.getMessage());
            }
        } else {
            MediaDeployer.getConsole("SmartDB Hot Deployer is not running");
        }
    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
    }

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
    }
}

class ProcessUtil {

    static interface Kernel32 extends Library {
        public static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        public int GetProcessId(Long hProcess);
    }

    public static int getPid(Process process) {
        Field filed;
        if (com.sun.jna.Platform.isWindows()) {
            try {
                filed = process.getClass().getDeclaredField("handle");
                filed.setAccessible(true);
                int pid = Kernel32.INSTANCE.GetProcessId((Long) filed.get(process));
                return pid;
            } catch (Exception e) {
                MediaDeployer.getConsole(e.getMessage());
            }
        } else if (com.sun.jna.Platform.isMac()) {
            try {
                filed = process.getClass().getDeclaredField("pid");
                filed.setAccessible(true);
                int pid = (Integer) filed.get(process);
                return pid;
            } catch (Exception e) {
                MediaDeployer.getConsole(e.getMessage());
            }
        }
        return 0;
    }
}