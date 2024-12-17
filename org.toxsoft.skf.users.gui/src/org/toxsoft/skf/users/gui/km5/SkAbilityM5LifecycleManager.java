package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Lifecylce manager for {@link SkAbilityM5Model}.
 *
 * @author Slavage
 */
public class SkAbilityM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkAbility> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkAbilityM5LifecycleManager( IM5Model<ISkAbility> aModel, ISkConnection aMaster ) {
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
  protected ISkAbility doCreate( IM5Bunch<ISkAbility> aValues ) {
    DtoSkAbility dtoAbility = makeAbilityDto( aValues );
    abilityManager().defineAbility( dtoAbility );
    return aValues.originalEntity();
  }

  @Override
  protected ISkAbility doEdit( IM5Bunch<ISkAbility> aValues ) {
    DtoSkAbility dtoAbility = makeAbilityDto( aValues );
    abilityManager().defineAbility( dtoAbility );
    return aValues.originalEntity();
  }

  @Override
  protected void doRemove( ISkAbility aEntity ) {
    abilityManager().removeAbility( aEntity.id() );
  }

  @Override
  protected IList<ISkAbility> doListEntities() {
    return abilityManager().listAbilities();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  // Timeable define.
  private static final String OPID_KIND_ID = "ability.kind.id"; //$NON-NLS-1$

  static DtoSkAbility makeAbilityDto( IM5Bunch<ISkAbility> aValues ) {
    String id = aValues.getAsAv( AID_STRID ).asString();
    String kindId = aValues.getAsAv( OPID_KIND_ID ).asString();
    String name = aValues.getAsAv( AID_NAME ).asString();
    String description = aValues.getAsAv( AID_DESCRIPTION ).asString();
    DtoSkAbility dtoAbility = DtoSkAbility.create( id, kindId, name, description );
    return dtoAbility;
  }

}
