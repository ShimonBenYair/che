/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.actions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.CoreLocalizationConstant;
import org.eclipse.che.ide.Resources;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.download.DownloadContainer;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Collections.singletonList;
import static org.eclipse.che.ide.api.resources.Resource.PROJECT;
import static org.eclipse.che.ide.workspace.perspectives.project.ProjectPerspective.PROJECT_PERSPECTIVE_ID;

/**
 * Download selected root project in the application context.
 *
 * @author Roman Nikitenko
 * @author Dmitry Shnurenko
 * @author Vlad Zhukovskyi
 * @see AppContext#getRootProject()
 */
@Singleton
public class DownloadProjectAction extends AbstractPerspectiveAction {

    private final AppContext               appContext;
    private       DownloadContainer        downloadContainer;

    @Inject
    public DownloadProjectAction(AppContext appContext,
                                 CoreLocalizationConstant locale,
                                 Resources resources,
                                 DownloadContainer downloadContainer) {
        super(singletonList(PROJECT_PERSPECTIVE_ID),
              locale.downloadProjectAsZipName(),
              locale.downloadProjectAsZipDescription(),
              null,
              resources.downloadZip());
        this.appContext = appContext;
        this.downloadContainer = downloadContainer;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        final Project project = appContext.getRootProject();

        checkState(project != null, "Null project occurred");

        downloadContainer.setUrl(project.getURL());
    }

    /** {@inheritDoc} */
    @Override
    public void updateInPerspective(@NotNull ActionEvent e) {
        final Resource[] resources = appContext.getResources();

        e.getPresentation().setVisible(true);
        e.getPresentation().setEnabled(resources != null && resources.length == 1 && resources[0].getResourceType() == PROJECT);
    }
}
