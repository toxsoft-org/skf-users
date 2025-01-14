package org.toxsoft.skf.users.mws;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkUsersMwsConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PARTSTACKID_SK_USERS_MAIN_EXTENSION     = "org.toxsoft.uskat.users.partstack.main_extension";            //$NON-NLS-1$
  String E4_VISUAL_ELEM_ID_PERSP_USERS_ROLES     = "org.toxsoft.uskat.users.persp.main";                          //$NON-NLS-1$
  String E4_VISUAL_ELEM_ID_MENU_ITEM_USERS_ROLES = "org.toxsoft.skf.users.mws.handledmenuitem.persp_skusersmain"; //$NON-NLS-1$
  String E4_VISUAL_ELEM_ID_TOOL_ITEM_USERS_ROLES = "org.toxsoft.skf.users.mws.handledtoolitem.persp_skusersmain"; //$NON-NLS-1$
  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_"; //$NON-NLS-1$
  // String ICON_XXX = "xxx"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkUsersMwsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
