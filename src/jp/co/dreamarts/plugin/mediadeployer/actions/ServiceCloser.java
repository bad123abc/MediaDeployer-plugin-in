package jp.co.dreamarts.plugin.mediadeployer.actions;

import java.awt.Desktop;
import java.io.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ServiceCloser implements IObjectActionDelegate {

    @Override
    public void run(IAction arg0) {
      System.out.println(MediaDeployer.getStartProcess().toString());
      MediaDeployer.getStartProcess().destroy();
      new MediaDeployer().getConsole("HIBIKI has been closed");
    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {

    }

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {

    }
}
