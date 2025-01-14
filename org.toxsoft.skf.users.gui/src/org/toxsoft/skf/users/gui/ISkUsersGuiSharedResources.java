package org.toxsoft.skf.users.gui;

import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.skf.users.gui.panels.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkUsersGuiSharedResources {

  /**
   * {@link SkUserM5Model}
   */
  String STR_N_USER        = Messages.getString( "STR_N_USER" );        //$NON-NLS-1$
  String STR_D_USER        = Messages.getString( "STR_D_USER" );        //$NON-NLS-1$
  String STR_N_FDEF_NAME   = Messages.getString( "STR_N_FDEF_NAME" );   //$NON-NLS-1$
  String STR_D_FDEF_NAME   = Messages.getString( "STR_D_FDEF_NAME" );   //$NON-NLS-1$
  String STR_N_FDEF_LOGIN  = Messages.getString( "STR_N_FDEF_LOGIN" );  //$NON-NLS-1$
  String STR_D_FDEF_LOGIN  = Messages.getString( "STR_D_FDEF_LOGIN" );  //$NON-NLS-1$
  String STR_N_FDEF_ACTIVE = Messages.getString( "STR_N_FDEF_ACTIVE" ); //$NON-NLS-1$
  String STR_D_FDEF_ACTIVE = Messages.getString( "STR_D_FDEF_ACTIVE" ); //$NON-NLS-1$
  String STR_N_FDEF_HIDDEN = Messages.getString( "STR_N_FDEF_HIDDEN" ); //$NON-NLS-1$
  String STR_D_FDEF_HIDDEN = Messages.getString( "STR_D_FDEF_HIDDEN" ); //$NON-NLS-1$
  String STR_N_FDEF_DESCR  = Messages.getString( "STR_N_FDEF_DESCR" );  //$NON-NLS-1$
  String STR_D_FDEF_DESCR  = Messages.getString( "STR_D_FDEF_DESCR" );  //$NON-NLS-1$

  /**
   * {@link SkRoleM5Model}
   */
  String STR_N_ROLE    = Messages.getString( "STR_N_ROLE" );    //$NON-NLS-1$
  String STR_D_ROLE    = Messages.getString( "STR_D_ROLE" );    //$NON-NLS-1$
  String STR_N_ROLE_ID = Messages.getString( "STR_N_ROLE_ID" ); //$NON-NLS-1$
  String STR_D_ROLE_ID = Messages.getString( "STR_D_ROLE_ID" ); //$NON-NLS-1$

  /**
   * {@link SkAbilityM5Model}
   */
  String STR_N_ABILITY      = Messages.getString( "STR_N_ABILITY" );      //$NON-NLS-1$
  String STR_D_ABILITY      = Messages.getString( "STR_D_ABILITY" );      //$NON-NLS-1$
  String STR_N_ABILITY_ID   = Messages.getString( "STR_N_ABILITY_ID" );   //$NON-NLS-1$
  String STR_D_ABILITY_ID   = Messages.getString( "STR_D_ABILITY_ID" );   //$NON-NLS-1$
  String STR_N_FDEF_ENABLE  = Messages.getString( "STR_N_FDEF_ENABLE" );  //$NON-NLS-1$
  String STR_D_FDEF_ENABLE  = Messages.getString( "STR_D_FDEF_ENABLE" );  //$NON-NLS-1$
  String STR_N_ABILITY_KIND = Messages.getString( "STR_N_ABILITY_KIND" ); //$NON-NLS-1$
  String STR_D_ABILITY_KIND = Messages.getString( "STR_D_ABILITY_KIND" ); //$NON-NLS-1$

  /**
   * {@link SkUserM5LifecycleManager}
   */
  String MSG_ERR_LOGIN_NOT_IDPATH = Messages.getString( "MSG_ERR_LOGIN_NOT_IDPATH" ); //$NON-NLS-1$

  /**
   * {@link SkUserMpc}
   */
  String STR_N_TMI_BY_ROLES = Messages.getString( "STR_N_TMI_BY_ROLES" ); //$NON-NLS-1$
  String STR_D_TMI_BY_ROLES = Messages.getString( "STR_D_TMI_BY_ROLES" ); //$NON-NLS-1$

  /**
   * {@link SkAbilityMpc}
   */
  String STR_N_TMI_BY_KINDS = Messages.getString( "STR_N_TMI_BY_KINDS" ); //$NON-NLS-1$
  String STR_D_TMI_BY_KINDS = Messages.getString( "STR_D_TMI_BY_KINDS" ); //$NON-NLS-1$

  /**
   * {@link ISkUsersGuiConstants}
   */
  String STR_N_NO_HIDDEN_USERS = Messages.getString( "STR_N_NO_HIDDEN_USERS" ); //$NON-NLS-1$
  String STR_D_NO_HIDDEN_USERS = Messages.getString( "STR_D_NO_HIDDEN_USERS" ); //$NON-NLS-1$
  String STR_N_CHANGE_PASSWORD = Messages.getString( "STR_N_CHANGE_PASSWORD" ); //$NON-NLS-1$
  String STR_D_CHANGE_PASSWORD = Messages.getString( "STR_D_CHANGE_PASSWORD" ); //$NON-NLS-1$

  /**
   * {@link PanelSkUsersEditor}
   */
  String STR_TAB_USER_PROPERTIES = Messages.getString( "STR_TAB_USER_PROPERTIES" ); //$NON-NLS-1$
  String STR_TAB_USER_ROLES      = Messages.getString( "STR_TAB_USER_ROLES" );      //$NON-NLS-1$

  /**
   * {@link PanelSkRolesEditor}
   */
  String STR_TAB_ROLE_PROPERTIES = Messages.getString( "STR_TAB_ROLE_PROPERTIES" ); //$NON-NLS-1$
  String STR_TAB_ROLE_ABILITIES  = Messages.getString( "STR_TAB_ROLE_ABILITIES" );  //$NON-NLS-1$
  String STR_TAB_ROLE_ACCESS     = Messages.getString( "STR_TAB_ROLE_ACCESS" );     //$NON-NLS-1$

}
