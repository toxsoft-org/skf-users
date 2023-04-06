package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.IM5Bunch;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.core.api.objserv.IDtoObject;
import org.toxsoft.uskat.core.api.users.ISkRole;
import org.toxsoft.uskat.core.api.users.ISkUserService;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.km5.KM5LifecycleManagerBasic;
import org.toxsoft.uskat.core.impl.dto.DtoObject;

/**
 * Lifecylce manager for {@link SkRoleM5Model}.
 *
 * @author dima
 */
public class SkRoleM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkRole, ISkConnection> {

  // FIXME create SkRoleMpc with filering out hidden roles

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkRoleM5LifecycleManager( IM5Model<ISkRole> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IDtoObject makeRoleDto( IM5Bunch<ISkRole> aValues ) {
    // Создаем IDpuObject и инициализируем его значениями из пучка
    String id = aValues.getAsAv( AID_STRID ).asString();
    Skid skid = new Skid( ISkRole.CLASS_ID, id );
    DtoObject dtoRole = DtoObject.createDtoObject( skid, coreApi() );
    dtoRole.attrs().setValue( ATRID_ROLE_IS_ENABLED, aValues.getAsAv( ATRID_ROLE_IS_ENABLED ) );
    dtoRole.attrs().setValue( ATRID_ROLE_IS_HIDDEN, aValues.getAsAv( ATRID_ROLE_IS_HIDDEN ) );
    dtoRole.attrs().setValue( FID_NAME, aValues.getAsAv( FID_NAME ) );
    dtoRole.attrs().setValue( FID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    return dtoRole;
  }

  private ISkUserService userService() {
    return master().coreApi().userService();
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkRole> aValues ) {
    IDtoObject dtoRole = makeRoleDto( aValues );
    return userService().svs().validator().canCreateRole( dtoRole );
  }

  @Override
  protected ISkRole doCreate( IM5Bunch<ISkRole> aValues ) {
    IDtoObject dtoRole = makeRoleDto( aValues );
    return userService().defineRole( dtoRole );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkRole> aValues ) {
    IDtoObject dtoRole = makeRoleDto( aValues );
    return userService().svs().validator().canEditRole( dtoRole, aValues.originalEntity() );
  }

  @Override
  protected ISkRole doEdit( IM5Bunch<ISkRole> aValues ) {
    IDtoObject dtoRole = makeRoleDto( aValues );
    return userService().defineRole( dtoRole );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkRole aEntity ) {
    return userService().svs().validator().canRemoveRole( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkRole aEntity ) {
    userService().removeRole( aEntity.id() );
  }

  @Override
  protected IList<ISkRole> doListEntities() {
    return userService().listRoles();
  }

}
