package org.toxsoft.skf.users.gui;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.skf.users.gui.km5.KM5UsersContributor;
import org.toxsoft.uskat.core.gui.km5.KM5Utils;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkUsersGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkUsersGui() {
    super( QuantSkUsersGui.class.getSimpleName() );
    KM5Utils.registerContributorCreator( KM5UsersContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkUsersGuiConstants.init( aWinContext );
  }

}
