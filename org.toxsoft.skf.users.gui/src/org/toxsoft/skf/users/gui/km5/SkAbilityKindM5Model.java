package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of {@link ISkAbilityKind}.
 *
 * @author Slavage
 */
public class SkAbilityKindM5Model
    extends KM5ModelBasic<ISkAbilityKind> {

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkAbilityKindM5Model( ISkConnection aConn ) {
    super( ISkAbilityKind.CLASS_ID, ISkAbilityKind.class, aConn );
    setNameAndDescription( STR_N_ABILITY_KIND, STR_D_ABILITY_KIND );
    // attributes
    ISkClassInfo cinf = skSysdescr().getClassInfo( ISkAbilityKind.CLASS_ID );
    // links
    KM5MultiLinkFieldDef abilities = //
        new KM5MultiLinkFieldDef( cinf.links().list().getByKey( LNKID_ABILITIES_OF_KIND ) );
    // add fields
    addFieldDefs( NAME, DESCRIPTION, abilities );
  }

  @Override
  protected IM5LifecycleManager<ISkAbilityKind> doCreateDefaultLifecycleManager() {
    ISkConnection master = domain().tsContext().get( ISkConnection.class );
    return new SkAbilityKindM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<ISkAbilityKind> doCreateLifecycleManager( Object aMaster ) {
    ISkConnection master = ISkConnection.class.cast( aMaster );
    return new SkAbilityKindM5LifecycleManager( this, master );
  }
}
