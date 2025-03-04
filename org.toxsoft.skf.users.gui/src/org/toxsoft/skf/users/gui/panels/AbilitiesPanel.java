package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skf.users.gui.incub.*;
import org.toxsoft.skf.users.gui.km5.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.api.users.ability.*;

/**
 * Implemention for {@link IAbilitiesPanel}
 *
 * @author Slavage
 */
public class AbilitiesPanel
    extends AbstractSkLazyControl
    implements IAbilitiesPanel {

  private ISkRole                role;
  private IGenericChangeListener checksChangeListener;
  private SkAbilityMpc           panelAbilities;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - ts context
   */
  public AbilitiesPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  public void setRole( ISkRole aRole ) {
    role = aRole;

    // When immutable role then non editable abilities!
    panelAbilities.setEditable( !ISkUserServiceHardConstants.isImmutableRole( role.id() ) );
    panelAbilities.getControl().setEnabled( !ISkUserServiceHardConstants.isImmutableRole( role.id() ) );

    initializeRoleAbilities();
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    IM5Model<ISkAbility> model = m5().getModel( ISkAbility.CLASS_ID, ISkAbility.class );
    IM5LifecycleManager<ISkAbility> lm = new SkAbilityM5LifecycleManager( model, skConn() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );

    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AV_TRUE );

    checksChangeListener = aSource -> changeRoleAbilities();

    panelAbilities = new SkAbilityMpc( ctx, model, lm.itemsProvider(), lm );
    panelAbilities.tree().checks().checksChangeEventer().addListener( checksChangeListener );

    initializeRoleAbilities();

    return panelAbilities.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkAbilityManager abilityManager() {
    return coreApi().userService().abilityManager();
  }

  /**
   * Initializing abilities of role.
   */
  private void initializeRoleAbilities() {
    IListEdit<ISkAbility> enableAbilities = new ElemArrayList<>();
    IListEdit<ISkAbility> disableAbilities = new ElemArrayList<>();
    for( ISkAbility ability : panelAbilities.tree().items() ) {
      if( abilityManager().isAbilityAllowed( role.id(), ability.id() ) ) {
        enableAbilities.add( ability );
      }
      else {
        disableAbilities.add( ability );
      }
    }
    ITsCheckSupport<ISkAbility> checks = panelAbilities.tree().checks();
    checks.checksChangeEventer().muteListener( checksChangeListener );
    checks.setItemsCheckState( enableAbilities, true );
    checks.setItemsCheckState( disableAbilities, false );
    checks.checksChangeEventer().unmuteListener( checksChangeListener );
    panelAbilities.tree().refresh();
  }

  /**
   * Changing abilities of role.
   */
  private void changeRoleAbilities() {
    if( role == null ) {
      return;
    }
    if( ISkUserServiceHardConstants.isImmutableRole( role.id() ) ) {
      // When immutable role then non editable abilities!
      return;
    }

    IStringListEdit enableAbilityIds = new StringLinkedBundleList();
    for( ISkAbility ability : panelAbilities.tree().checks().listCheckedItems( true ) ) {
      enableAbilityIds.add( ability.id() );
    }
    abilityManager().setRoleAbilities( role.id(), enableAbilityIds );
  }

}
