package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.inpled.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * Self-contaioned panel to edit roles {@link ISkUserService#listRoles()}.
 *
 * @author hazard157
 */
public class PanelSkRolesEditor
    extends AbstractSkStdEventsProducerLazyPanel<ISkRole> {

  private IM5CollectionPanel<ISkRole> panelRoles;
  private IInplaceEditorPanel         inplaceRoleDetail;
  private IM5EntityPanel<ISkRole>     panelRoleDetail;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUsedConnId {@link IdChain} - ID of connection to be used, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkRolesEditor( ITsGuiContext aContext, IdChain aUsedConnId ) {
    super( aContext, aUsedConnId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkStdEventsProducerLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    SashForm sf = new SashForm( aParent, SWT.HORIZONTAL );

    IM5Model<ISkRole> model = m5().getModel( ISkRole.CLASS_ID, ISkRole.class );
    IM5LifecycleManager<ISkRole> lm = new SkRoleM5LifecycleManager( model, skConn() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );

    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    panelRoles = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    panelRoles.addTsSelectionListener( selectionChangeEventHelper );
    panelRoles.addTsDoubleClickListener( doubleClickEventHelper );

    panelRoles.createControl( sf );
    panelRoles.getControl().setLayoutData( BorderLayout.CENTER );

    ITsGuiContext ctx2 = new TsGuiContext( tsContext() );
    panelRoleDetail = model.panelCreator().createEntityEditorPanel( ctx2, lm );
    panelRoleDetail.setEditable( false );

    AbstractContentPanel contentPanel = new InplaceContentM5EntityPanelWrapper<>( ctx2, panelRoleDetail );
    inplaceRoleDetail = new InplaceEditorContainerPanel( ctx2, contentPanel );

    inplaceRoleDetail.createControl( sf );
    inplaceRoleDetail.getControl().setLayoutData( BorderLayout.CENTER );
    // panelRoleDetail.createControl( sf );

    // panelRoles.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
    // panelRoleDetail.setEntity( aSelectedItem );
    // } );

    // if( panelRoles.items().size() > 0 ) {
    // panelRoles.setSelectedItem( panelRoles.items().first() );
    // }
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  protected ISkRole doGetSelectedItem() {
    return panelRoles.selectedItem();
  }

  @Override
  protected void doSetSelectedItem( ISkRole aItem ) {
    panelRoles.setSelectedItem( aItem );
  }

}
