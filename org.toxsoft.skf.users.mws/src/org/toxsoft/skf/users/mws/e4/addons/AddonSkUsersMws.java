package org.toxsoft.skf.users.mws.e4.addons;

import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;
import static org.toxsoft.skf.users.mws.ISkUsersMwsConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.rcp.Activator;
import org.toxsoft.skf.users.mws.*;
import org.toxsoft.skf.users.mws.e4.service.*;
import org.toxsoft.uskat.core.gui.utils.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Plugin's addon.
 *
 * @author hazard157
 */
public class AddonSkUsersMws
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkUsersMws() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkUsersMwsConstants.init( aWinContext );
    //
    ICurrentUsersMwsSkRoleService currentUsersMwsSkRoleService = new CurrentUsersMwsSkRoleService();
    aWinContext.set( ICurrentUsersMwsSkRoleService.class, currentUsersMwsSkRoleService );
    ICurrentUsersMwsSkUserService currentUsersMwsSkUserService = new CurrentUsersMwsSkUserService();
    aWinContext.set( ICurrentUsersMwsSkUserService.class, currentUsersMwsSkUserService );
    // implement access rights
    GuiE4ElementsToAbilitiesBinder binder = new GuiE4ElementsToAbilitiesBinder( new TsGuiContext( aWinContext ) );
    binder.bindPerspective( ABILITYID_USER_ROLE_EDITOR, E4_VISUAL_ELEM_ID_PERSP_USERS_ROLES );
    binder.bindMenuElement( ABILITYID_USER_ROLE_EDITOR, E4_VISUAL_ELEM_ID_MENU_ITEM_USERS_ROLES );
    binder.bindToolItem( ABILITYID_USER_ROLE_EDITOR, E4_VISUAL_ELEM_ID_TOOL_ITEM_USERS_ROLES );
    SkCoreUtils.registerCoreApiHandler( binder );
  }

}
