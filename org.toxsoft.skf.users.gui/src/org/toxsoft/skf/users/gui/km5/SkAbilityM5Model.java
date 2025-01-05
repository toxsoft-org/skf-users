package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of {@link ISkAbility}.
 *
 * @author Slavage
 */
public class SkAbilityM5Model
    extends KM5ModelBasic<ISkAbility> {

  public static final String AID_ENABLE = "enable"; //$NON-NLS-1$

  // public final IM5AttributeFieldDef<ISkAbility> ROLEID =
  // new M5AttributeFieldDef<>( AID_ENABLE, IAvMetaConstants.DDEF_STRING, //
  // TSID_NAME, "", //
  // TSID_DESCRIPTION, "", //
  // TSID_DEFAULT_VALUE, avStr( NONE_ID ) //
  // ) {
  //
  // @Override
  // protected void doInit() {
  // setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
  // }
  //
  // @Override
  // protected IAtomicValue doGetFieldValue( ISkAbility aEntity ) {
  // return AvUtils.avStr( aEntity. );
  // }
  //
  // };

  // public final IM5AttributeFieldDef<ISkAbility> ENABLE =
  // new M5AttributeFieldDef<>( AID_ENABLE, IAvMetaConstants.DDEF_TS_BOOL, //
  // TSID_NAME, STR_N_FDEF_ENABLE, //
  // TSID_DESCRIPTION, STR_D_FDEF_ENABLE, //
  // TSID_DEFAULT_VALUE, avStr( NONE_ID ) //
  // ) {
  //
  // @Override
  // protected void doInit() {
  // setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
  // }
  //
  // @Override
  // protected IAtomicValue doGetFieldValue( ISkAbility aEntity ) {
  // return AvUtils.avBool( abilityManager().isAbilityAllowed( aEntity.id() ) );
  // }
  //
  // };

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkAbilityM5Model( ISkConnection aConn ) {
    super( ISkAbility.CLASS_ID, ISkAbility.class, aConn );
    setNameAndDescription( STR_N_ABILITY, STR_D_ABILITY );
    // attributes
    ISkClassInfo cinf = skSysdescr().getClassInfo( ISkAbility.CLASS_ID );

    // GOGA --- ability enabled state was removed from API
    // KM5AttributeFieldDef<ISkAbility> enable = //
    // new KM5AttributeFieldDef<>( cinf.attrs().list().getByKey( ATRID_ABILITY_IS_ENABLED ) );
    // enable.setNameAndDescription( STR_N_FDEF_ENABLE, STR_D_FDEF_ENABLE );
    // enable.setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    // addFieldDefs( NAME, enable, DESCRIPTION );
    // ---

    // add fields
    addFieldDefs( NAME, DESCRIPTION );// , ENABLE );
  }

  @Override
  protected IM5LifecycleManager<ISkAbility> doCreateDefaultLifecycleManager() {
    ISkConnection master = domain().tsContext().get( ISkConnection.class );
    return new SkAbilityM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<ISkAbility> doCreateLifecycleManager( Object aMaster ) {
    ISkConnection master = ISkConnection.class.cast( aMaster );
    return new SkAbilityM5LifecycleManager( this, master );
  }

  private ISkAbilityManager abilityManager() {
    return coreApi().userService().abilityManager();
  }

}
