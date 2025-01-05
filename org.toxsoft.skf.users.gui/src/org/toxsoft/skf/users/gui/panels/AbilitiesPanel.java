package org.toxsoft.skf.users.gui.panels;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
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

  private ISkRole role;

  private SkAbilityMpc panelAbilities;

  public AbilitiesPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  public void setRole( ISkRole aRole ) {
    role = aRole;
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

    panelAbilities = new SkAbilityMpc( ctx, model, lm.itemsProvider(), lm );
    panelAbilities.tree().checks().checksChangeEventer().addListener( aSource -> {
      // Changing abilities state.
      if( role == null ) {
        return;
      }
      //
      IStringListEdit abilityIds = new StringLinkedBundleList();
      for( ISkAbility ability : panelAbilities.tree().checks().listCheckedItems( false ) ) {
        abilityIds.add( ability.id() );
      }
      abilityManager().setRoleAbilities( role.id(), abilityIds, false );
      //
      abilityIds.clear();
      for( ISkAbility ability : panelAbilities.tree().checks().listCheckedItems( true ) ) {
        abilityIds.add( ability.id() );
      }
      abilityManager().setRoleAbilities( role.id(), abilityIds, true );
    } );

    initializeAbilitiesChecks();

    return panelAbilities.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkAbilityManager abilityManager() {
    return coreApi().userService().abilityManager();
  }

  private void initializeAbilitiesChecks() {
    for( ISkAbility ability : panelAbilities.tree().items() ) {
      panelAbilities.tree().checks().setItemCheckState( ability,
          abilityManager().isAbilityAllowed( role.id(), ability.id() ) );
    }
  }

}
