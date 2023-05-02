package org.toxsoft.skf.users.skide.main;

import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;
import static org.toxsoft.skf.users.skide.ISkfUsersSkideSharedResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.skf.users.gui.panels.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 */
class SkideUnitPanelUsers
    extends AbstractSkideUnitPanel {

  public SkideUnitPanelUsers( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TabFolder tabFolder = new TabFolder( aParent, SWT.TOP );
    EIconSize tabIconSize = hdpiService().getJFaceCellIconsSize();
    // users tab
    TabItem tiUsers = new TabItem( tabFolder, SWT.NONE );
    PanelSkUsersEditor panelUsers = new PanelSkUsersEditor( tsContext(), null );
    tiUsers.setControl( panelUsers.createControl( tabFolder ) );
    tiUsers.setText( STR_TAB_USERS );
    tiUsers.setToolTipText( STR_TAB_USERS_D );
    tiUsers.setImage( iconManager().loadStdIcon( ICONID_USERS_LIST, tabIconSize ) );
    // roles tab
    TabItem tiRoles = new TabItem( tabFolder, SWT.NONE );
    PanelSkRolesEditor panelRoles = new PanelSkRolesEditor( tsContext(), null );
    tiRoles.setControl( panelRoles.createControl( tabFolder ) );
    tiRoles.setText( STR_TAB_ROLES );
    tiRoles.setToolTipText( STR_TAB_ROLES_D );
    tiRoles.setImage( iconManager().loadStdIcon( ICONID_ROLES_LIST, tabIconSize ) );
    return tabFolder;
  }

}
