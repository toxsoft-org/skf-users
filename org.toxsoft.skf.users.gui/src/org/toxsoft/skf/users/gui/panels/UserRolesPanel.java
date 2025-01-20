package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.users.gui.incub.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * @author Slavage
 */
public class UserRolesPanel
    extends AbstractSkLazyControl
    implements IUserRolesPanel {

  /**
   * M5-model of {@link ISkRole}.
   *
   * @author Slavage
   */
  class InnerM5Model
      extends KM5ModelBasic<ISkRole> {

    public static String CLASS_ID = ISkUserServiceHardConstants.CLSID_ROLE + ".local"; //$NON-NLS-1$

    public static final String AID_ROLE_NAME        = "RoleName";        //$NON-NLS-1$
    public static final String AID_ROLE_DESCRIPTION = "RoleDescription"; //$NON-NLS-1$

    public final IM5AttributeFieldDef<ISkRole> ROLE_NAME = new M5AttributeFieldDef<>( AID_ROLE_NAME, STRING, //
        TSID_NAME, STR_N_FDEF_NAME, //
        TSID_DESCRIPTION, STR_D_FDEF_NAME, //
        TSID_DEFAULT_VALUE, avStr( NONE_ID ) //
    ) {

      @Override
      protected void doInit() {
        setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      }

      @Override
      protected String doGetFieldValueName( ISkRole aRole ) {
        return aRole.nmName();
      }

      @Override
      protected IAtomicValue doGetFieldValue( ISkRole aEntity ) {
        return AvUtils.avStr( aEntity.nmName() );
      }
    };

    public final IM5AttributeFieldDef<ISkRole> ROLE_DESCRIPTION =
        new M5AttributeFieldDef<>( AID_ROLE_DESCRIPTION, STRING, //
            TSID_NAME, STR_N_FDEF_DESCR, //
            TSID_DESCRIPTION, STR_N_FDEF_DESCR, //
            TSID_DEFAULT_VALUE, avStr( NONE_ID ) //
        ) {

          @Override
          protected void doInit() {
            setFlags( M5FF_DETAIL | M5FF_READ_ONLY );
          }

          @Override
          protected String doGetFieldValueName( ISkRole aRole ) {
            return aRole.description();
          }

          @Override
          protected IAtomicValue doGetFieldValue( ISkRole aEntity ) {
            return AvUtils.avStr( aEntity.description() );
          }
        };

    /**
     * Constructor.
     *
     * @param aConn {@link ISkConnection} - the connection
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     */
    public InnerM5Model( ISkConnection aConn ) {
      super( CLASS_ID, ISkRole.class, aConn );
      setNameAndDescription( STR_N_ROLE, STR_D_ROLE );
      // fields
      addFieldDefs( ROLE_NAME, ROLE_DESCRIPTION );
    }

    @Override
    protected IM5LifecycleManager<ISkRole> doCreateDefaultLifecycleManager() {
      ISkConnection master = domain().tsContext().get( ISkConnection.class );
      return new SkRoleM5LifecycleManager( this, master );
    }

    @Override
    protected IM5LifecycleManager<ISkRole> doCreateLifecycleManager( Object aMaster ) {
      ISkConnection master = ISkConnection.class.cast( aMaster );
      return new SkRoleM5LifecycleManager( this, master );
    }

  }

  private ISkUser                     user;
  private IGenericChangeListener      checksChangeListener;
  private IM5CollectionPanel<ISkRole> panelRoles;

  /**
   * Constructor.
   *
   * @param aContext GUI components editable context.
   */
  public UserRolesPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  public void setUser( ISkUser aUser ) {
    user = aUser;
    if( user == null ) {
      return;
    }

    initializeUserRoles();
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // Using temporary model.
    // InnerM5Model model = new InnerM5Model( skConn() );
    // m5().initTemporaryModel( model );
    IM5Model<ISkRole> model = m5().getModel( ISkRole.CLASS_ID, ISkRole.class );

    IM5LifecycleManager<ISkRole> lm = new SkRoleM5LifecycleManager( model, skConn() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );

    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AV_TRUE );

    checksChangeListener = aSource -> changeUserRoles();

    panelRoles = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panelRoles.checkSupport().checksChangeEventer().addListener( checksChangeListener );

    initializeUserRoles();

    return panelRoles.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkUserService userService() {
    return coreApi().userService();
  }

  /**
   * Initializing roles of user.
   */
  private void initializeUserRoles() {
    IListEdit<ISkRole> enableRoles = new ElemArrayList<ISkRole>();
    IListEdit<ISkRole> disableRoles = new ElemArrayList<ISkRole>();
    for( ISkRole role : panelRoles.items() ) {
      if( user.listRoles().findByKey( role.id() ) != null ) {
        enableRoles.add( role );
      }
      else {
        disableRoles.add( role );
      }
    }
    ITsCheckSupport<ISkRole> checks = panelRoles.checkSupport();
    checks.checksChangeEventer().muteListener( checksChangeListener );
    checks.setItemsCheckState( enableRoles, true );
    checks.setItemsCheckState( disableRoles, false );
    checks.checksChangeEventer().unmuteListener( checksChangeListener );
    panelRoles.refresh();
  }

  /**
   * Changing roles of user.
   */
  private void changeUserRoles() {
    if( user == null ) {
      return;
    }
    if( ISkUserServiceHardConstants.isImmutableUser( user.id() ) ) {
      // When immutable user then non editable roles!
      return;
    }
    IStridablesListEdit<ISkRole> roles = new StridablesList<ISkRole>();
    ITsCheckSupport<ISkRole> checks = panelRoles.checkSupport();
    for( ISkRole role : panelRoles.items() ) {
      if( checks.getItemCheckState( role ) ) {
        roles.add( role );
      }
    }
    userService().setUserRoles( user.id(), roles );
  }

}
