package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Lifecylce manager for {@link SkAbilityKindM5Model}.
 *
 * @author Slavage
 */
public class SkAbilityKindM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkAbilityKind> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkAbilityKindM5LifecycleManager( IM5Model<ISkAbilityKind> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkAbilityManager abilityManager() {
    return master().coreApi().userService().abilityManager();
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ISkAbilityKind doCreate( IM5Bunch<ISkAbilityKind> aValues ) {
    DtoSkAbilityKind dtoKind = makeKindDto( aValues );
    abilityManager().defineKind( dtoKind );
    return aValues.originalEntity();
  }

  @Override
  protected ISkAbilityKind doEdit( IM5Bunch<ISkAbilityKind> aValues ) {
    DtoSkAbilityKind dtoKind = makeKindDto( aValues );
    abilityManager().defineKind( dtoKind );
    return aValues.originalEntity();
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkAbilityKind aEntity ) {
    return abilityManager().svs().validator().canRemoveKind( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkAbilityKind aEntity ) {
    abilityManager().removeKind( aEntity.id() );
  }

  @Override
  protected IList<ISkAbilityKind> doListEntities() {
    return abilityManager().listKinds();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  static DtoSkAbilityKind makeKindDto( IM5Bunch<ISkAbilityKind> aValues ) {
    String id = aValues.getAsAv( AID_STRID ).asString();
    String name = aValues.getAsAv( AID_NAME ).asString();
    String description = aValues.getAsAv( AID_DESCRIPTION ).asString();
    DtoSkAbilityKind dtoKind = DtoSkAbilityKind.create( id, name, description );
    return dtoKind;
  }

}
