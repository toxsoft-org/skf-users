package org.toxsoft.skf.users.gui;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkUsersGuiConstants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICONID_FIELD_NAME = "ICONID_";            //$NON-NLS-1$
  String ICONID_USER                 = "user";               //$NON-NLS-1$
  String ICONID_USERS_LIST           = "users-list";         //$NON-NLS-1$
  String ICONID_ROLE                 = "role";               //$NON-NLS-1$
  String ICONID_ROLES_LIST           = "roles-list";         //$NON-NLS-1$
  String ICONID_USER_EDITOR          = "app-users-editor";   //$NON-NLS-1$
  String ICONID_KINDS_LIST           = "ability-kinds-list"; //$NON-NLS-1$
  String ICONID_KIND                 = "ability";            //$NON-NLS-1$
  String ICONID_ABILITY              = "ability";            //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_NO_HIDDEN_USERS = SK_ID + ".users.gui.IsHiddenUsersShown"; //$NON-NLS-1$
  String ACTID_CHANGE_PASSWORD = SK_ID + ".users.gui.ChangePassword";     //$NON-NLS-1$

  TsActionDef ACDEF_NO_HIDDEN_USERS = TsActionDef.ofCheck2( ACTID_NO_HIDDEN_USERS, //
      STR_N_NO_HIDDEN_USERS, STR_D_NO_HIDDEN_USERS, ICONID_VIEW_FILTER );

  TsActionDef ACDEF_CHANGE_PASSWORD = TsActionDef.ofPush2( ACTID_CHANGE_PASSWORD, //
      STR_N_CHANGE_PASSWORD, STR_D_CHANGE_PASSWORD, ICONID_DIALOG_PASSWORD );

  /**
   * id тип возможности «Редактор пользователей и ролей»
   */
  String ABKINDID_USER_ROLES = ISkUserService.SERVICE_ID + ".abkind.user.roles"; //$NON-NLS-1$

  /**
   * создание «своего» типа
   */
  IDtoSkAbilityKind ABKIND_USER_ROLES =
      DtoSkAbilityKind.create( ABKINDID_USER_ROLES, STR_ABKIND_USER_ROLES, STR_ABKIND_USER_ROLES_D );

  /**
   * Create id ability to access user/roles editor
   */
  String ABILITYID_USER_ROLE_EDITOR = ISkUserService.SERVICE_ID + ".ability.user.roles.editor"; //$NON-NLS-1$

  /**
   * Create ability to access values editor
   */
  IDtoSkAbility ABILITY_ACCESS_USER_ROLE_EDITOR = DtoSkAbility.create( ABILITYID_USER_ROLE_EDITOR, ABKINDID_USER_ROLES,
      STR_ABILITY_ACCESS_USER_ROLE_EDITOR, STR_ABILITY_ACCESS_USER_ROLE_EDITOR_D );

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
