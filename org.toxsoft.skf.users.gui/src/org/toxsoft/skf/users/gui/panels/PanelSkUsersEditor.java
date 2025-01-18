package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.inpled.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * Self-contaioned panel to edit users {@link ISkUserService#listUsers()}.
 *
 * @author hazard157
 */
public class PanelSkUsersEditor
    extends AbstractSkStdEventsProducerLazyPanel<ISkUser> {

  /**
   * M5-model of {@link ISkUser}.
   *
   * @author Slavage
   */
  class InnerM5Model
      extends KM5ModelBasic<ISkUser> {

    public static String CLASS_ID = ISkUserServiceHardConstants.CLSID_USER + ".local"; //$NON-NLS-1$

    /**
     * Constructor.
     *
     * @param aConn {@link ISkConnection} - the connection
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     */
    public InnerM5Model( ISkConnection aConn ) {
      super( CLASS_ID, ISkUser.class, aConn );
      setNameAndDescription( STR_N_USER, STR_D_USER );
      // attributes
      ISkClassInfo cinf = skSysdescr().getClassInfo( ISkUser.CLASS_ID );
      KM5AttributeFieldDef<ISkUser> login = //
          new KM5AttributeFieldDef<>( cinf.attrs().list().getByKey( AID_STRID ) );
      login.setFlags( M5FF_INVARIANT );
      login.setNameAndDescription( STR_N_FDEF_LOGIN, STR_D_FDEF_LOGIN );
      KM5AttributeFieldDef<ISkUser> active = //
          new KM5AttributeFieldDef<>( cinf.attrs().list().getByKey( ATRID_USER_IS_ENABLED ) );
      active.setNameAndDescription( STR_N_FDEF_ACTIVE, STR_D_FDEF_ACTIVE );
      active.setFlags( M5FF_COLUMN );
      KM5AttributeFieldDef<ISkUser> hidden = //
          new KM5AttributeFieldDef<>( cinf.attrs().list().getByKey( ATRID_USER_IS_HIDDEN ) );
      hidden.setNameAndDescription( STR_N_FDEF_HIDDEN, STR_D_FDEF_HIDDEN );
      hidden.setFlags( M5FF_COLUMN );
      NAME.setNameAndDescription( STR_N_FDEF_NAME, STR_D_FDEF_NAME );
      DESCRIPTION.setNameAndDescription( STR_N_FDEF_DESCR, STR_D_FDEF_DESCR );
      // add fields
      addFieldDefs( login, NAME, active, hidden, DESCRIPTION );
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

  private SkUserMpc               panelUsers;
  private IInplaceEditorPanel     inplaceRoleDetail;
  private IM5EntityPanel<ISkUser> panelUserDetail;
  private UserRolesPanel          panelUserRoles;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUsedConnId {@link IdChain} - ID of connection to be used, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkUsersEditor( ITsGuiContext aContext, IdChain aUsedConnId ) {
    super( aContext, aUsedConnId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkStdEventsProducerLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    SashForm sf = new SashForm( aParent, SWT.HORIZONTAL );

    // Using temporary model.
    InnerM5Model model = new InnerM5Model( skConn() );
    m5().initTemporaryModel( model );
    // IM5Model<ISkUser> model = m5().getModel( ISkUser.CLASS_ID, ISkUser.class );

    IM5LifecycleManager<ISkUser> lm = new SkUserM5LifecycleManager( model, skConn() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );

    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );

    // panelUsers = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panelUsers = new SkUserMpc( ctx, model, lm.itemsProvider(), lm );
    panelUsers.addTsSelectionListener( selectionChangeEventHelper );
    panelUsers.addTsDoubleClickListener( doubleClickEventHelper );

    panelUsers.createControl( sf );
    panelUsers.getControl().setLayoutData( BorderLayout.CENTER );

    CTabFolder tabFolder = new CTabFolder( sf, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );

    // --- Panel 1.
    CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
    tabItem.setText( STR_TAB_USER_PROPERTIES );

    panelUserDetail = model.panelCreator().createEntityEditorPanel( ctx, lm );
    panelUserDetail.setEditable( false );
    AbstractContentPanel contentPanel = new InplaceContentM5EntityPanelWrapper<>( ctx, panelUserDetail );
    inplaceRoleDetail = new InplaceEditorContainerPanel( ctx, contentPanel );
    tabItem.setControl( inplaceRoleDetail.createControl( tabFolder ) );

    // --- Panel 2.
    CTabItem tabItem2 = new CTabItem( tabFolder, SWT.NONE );
    tabItem2.setText( STR_TAB_USER_ROLES );

    panelUserRoles = new UserRolesPanel( ctx );

    panelUserRoles.createControl( tabFolder );
    panelUserRoles.getControl().setLayoutData( BorderLayout.CENTER );

    tabItem2.setControl( panelUserRoles.getControl() );

    panelUsers.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      panelUserDetail.setEntity( aSelectedItem );
      panelUserRoles.setUser( aSelectedItem );
    } );

    // Prepare for view.
    panelUsers.setSelectedItem( panelUsers.tree().items().first() );
    tabFolder.setSelection( tabItem );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  protected ISkUser doGetSelectedItem() {
    return panelUsers.selectedItem();
  }

  @Override
  public void doSetSelectedItem( ISkUser aItem ) {
    panelUsers.setSelectedItem( aItem );
  }

}
