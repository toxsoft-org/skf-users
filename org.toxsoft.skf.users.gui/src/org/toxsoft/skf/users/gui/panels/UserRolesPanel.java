package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.users.gui.incub.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * @author Slavage
 */
public class UserRolesPanel
    extends AbstractSkLazyControl
    implements IUserRolesPanel {

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

    MultiPaneComponentModown<ISkRole> mpc = new MultiPaneComponentModown<>( ctx, model, lm.itemsProvider(), lm ) {

      protected void doCreateTreeColumns() {
        // Shows the following fields.
        tree().columnManager().add( FID_NAME );
        tree().columnManager().add( FID_DESCRIPTION );
      }

    };
    panelRoles = new M5CollectionPanelMpcModownWrapper<>( mpc, false );
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
