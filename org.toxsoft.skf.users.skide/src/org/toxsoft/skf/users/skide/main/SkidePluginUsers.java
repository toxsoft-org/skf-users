package org.toxsoft.skf.users.skide.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;
import static org.toxsoft.skf.users.skide.ISkfUsersSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: USkat users and roles management.
 *
 * @author hazard157
 */
public class SkidePluginUsers
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.users"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginUsers();

  SkidePluginUsers() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_USERS_MANAGEMENT, //
        TSID_DESCRIPTION, STR_SKIDE_USERS_MANAGEMENT_D, //
        TSID_ICON_ID, ICONID_USERS_LIST //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitUsers( aContext, this ) );
  }

}
