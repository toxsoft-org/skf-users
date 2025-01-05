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
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.impl.dto.*;

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
  private AbilitiesPanel              panelAbilities;

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
    prepareDebugAbilities();

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

    // --- Panel 1.
    CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
    tabItem.setText( "Свойства" );

    panelRoleDetail = model.panelCreator().createEntityEditorPanel( ctx, lm );
    panelRoleDetail.setEditable( false );
    AbstractContentPanel contentPanel = new InplaceContentM5EntityPanelWrapper<>( ctx, panelRoleDetail );
    inplaceRoleDetail = new InplaceEditorContainerPanel( ctx, contentPanel );
    tabItem.setControl( inplaceRoleDetail.createControl( tabFolder ) );

    // --- Panel 2.
    CTabItem tabItem2 = new CTabItem( tabFolder, SWT.NONE );
    tabItem2.setText( "Возможности" );

    panelAbilities = new AbilitiesPanel( ctx );
    tabItem2.setControl( panelAbilities.createControl( tabFolder ) );

    // --- Panel 3.
    CTabItem tabItem3 = new CTabItem( tabFolder, SWT.NONE );
    tabItem3.setText( "Матрица доступа" );

    panelRoles.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      panelRoleDetail.setEntity( aSelectedItem );
      panelAbilities.setRole( aSelectedItem );
    } );

    // Prepare for view.
    panelRoles.setSelectedItem( panelRoles.items().first() );
    tabFolder.setSelection( tabItem );
  }

  private ISkAbilityManager abilityManager() {
    return coreApi().userService().abilityManager();
  }

  private void prepareDebugAbilities() {
    IDtoSkAbilityKind kindDto = DtoSkAbilityKind.create( "AbilityKind1", "AbilityKind1", "AbilityKind1" );
    abilityManager().defineKind( kindDto );
    kindDto = DtoSkAbilityKind.create( "AbilityKind2", "AbilityKind2", "AbilityKind2" );
    abilityManager().defineKind( kindDto );

    IDtoSkAbility abilityDto = DtoSkAbility.create( "Ability1", "AbilityKind1", "Ability1", "Ability1" );
    abilityManager().defineAbility( abilityDto );
    abilityDto = DtoSkAbility.create( "Ability2", "AbilityKind1", "Ability2", "Ability2" );
    abilityManager().defineAbility( abilityDto );
    abilityDto = DtoSkAbility.create( "Ability3", "AbilityKind1", "Ability3", "Ability3" );
    abilityManager().defineAbility( abilityDto );

    abilityDto = DtoSkAbility.create( "Ability4", "AbilityKind2", "Ability4", "Ability4" );
    abilityManager().defineAbility( abilityDto );
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
