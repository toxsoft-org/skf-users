package org.toxsoft.skf.users.gui;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkUsersGuiConstants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICONID_FIELD_NAME = "ICONID_";    //$NON-NLS-1$
  String ICONID_USER                 = "user";       //$NON-NLS-1$
  String ICONID_USERS_LIST           = "users-list"; //$NON-NLS-1$
  String ICONID_ROLE                 = "role";       //$NON-NLS-1$
  String ICONID_ROLES_LIST           = "roles-list"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_NO_HIDDEN_USERS = SK_ID + ".users.gui.IsHiddenUsersShown"; //$NON-NLS-1$
  String ACTID_CHANGE_PASSWORD = SK_ID + ".users.gui.ChangePassword";     //$NON-NLS-1$

  TsActionDef ACDEF_NO_HIDDEN_USERS = TsActionDef.ofCheck2( ACTID_NO_HIDDEN_USERS, //
      STR_N_NO_HIDDEN_USERS, STR_D_NO_HIDDEN_USERS, ICONID_VIEW_FILTER );

  TsActionDef ACDEF_CHANGE_PASSWORD = TsActionDef.ofPush2( ACTID_CHANGE_PASSWORD, //
      STR_N_CHANGE_PASSWORD, STR_D_CHANGE_PASSWORD, ICONID_DIALOG_PASSWORD );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkUsersGuiConstants.class, PREFIX_OF_ICONID_FIELD_NAME );
  }

}
