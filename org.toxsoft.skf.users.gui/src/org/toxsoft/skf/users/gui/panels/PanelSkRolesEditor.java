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
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
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

    CTabFolder tabFolder = new CTabFolder( sf, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );

    panelRoleDetail = model.panelCreator().createEntityEditorPanel( ctx, lm );

    CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
    tabItem.setText( "Свойства" );

    TsComposite frameDetail = new TsComposite( tabFolder );
    frameDetail.setLayout( new BorderLayout() );

    tabItem.setControl( frameDetail );
    panelRoleDetail.createControl( frameDetail );

    CTabItem tabItem2 = new CTabItem( tabFolder, SWT.NONE );
    tabItem2.setText( "Возможности" );

    CTabItem tabItem3 = new CTabItem( tabFolder, SWT.NONE );
    tabItem3.setText( "Матрица доступа" );

    if( tabFolder.getSelectionIndex() < 0 ) {
      tabFolder.setSelection( 0 );
    }
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
    panelRoleDetail.setEntity( aItem );
  }

}
