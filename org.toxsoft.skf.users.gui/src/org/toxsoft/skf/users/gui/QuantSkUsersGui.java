package org.toxsoft.skf.users.gui;

import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkUsersGui
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantSkUsersGui() {
    super( QuantSkUsersGui.class.getSimpleName() );
    KM5Utils.registerContributorCreator( KM5UsersContributor.CREATOR );
    SkCoreUtils.registerCoreApiHandler( this );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkUsersGuiConstants.init( aWinContext );
  }

  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    // register abilities
    aCoreApi.userService().abilityManager().defineKind( ABKIND_USER_ROLES );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_ACCESS_USER_ROLE_EDITOR );
  }
}
