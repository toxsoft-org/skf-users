package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of {@link ISkUser}.
 *
 * @author Slavage
 */
public class SkUserRolesM5Model
    extends KM5ModelBasic<ISkUser> {

  public static String CLASS_ID = ISkUserServiceHardConstants.CLSID_USER + ".roles"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkUserRolesM5Model( ISkConnection aConn ) {
    super( CLASS_ID, ISkUser.class, aConn );
    setNameAndDescription( STR_N_USER, STR_D_USER );
    // attributes
    ISkClassInfo cinf = skSysdescr().getClassInfo( ISkUser.CLASS_ID );
    KM5AttributeFieldDef<ISkUser> login = //
        new KM5AttributeFieldDef<>( cinf.attrs().list().getByKey( AID_STRID ) );
    login.setFlags( M5FF_INVARIANT );
    login.setNameAndDescription( STR_N_FDEF_LOGIN, STR_D_FDEF_LOGIN );
    // links
    KM5MultiLinkFieldDef roles = //
        new KM5MultiLinkFieldDef( cinf.links().list().getByKey( LNKID_USER_ROLES ) );
    NAME.setNameAndDescription( STR_N_FDEF_NAME, STR_D_FDEF_NAME );
    DESCRIPTION.setNameAndDescription( STR_N_FDEF_DESCR, STR_D_FDEF_DESCR );
    // add fields
    addFieldDefs( login, roles );
  }

  @Override
  protected IM5LifecycleManager<ISkUser> doCreateDefaultLifecycleManager() {
    ISkConnection master = domain().tsContext().get( ISkConnection.class );
    return new SkUserM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<ISkUser> doCreateLifecycleManager( Object aMaster ) {
    ISkConnection master = ISkConnection.class.cast( aMaster );
    return new SkUserM5LifecycleManager( this, master );
  }

}
