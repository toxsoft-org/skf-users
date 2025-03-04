package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Lifecylce manager for {@link SkUserM5Model}.
 *
 * @author hazard157
 * @author dima
 */
public class SkUserM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkUser> {

  // TODO when editing user in roles selection dialog should be the hiddin roles shown? maybe app pref?

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkUserM5LifecycleManager( IM5Model<ISkUser> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkUserService userService() {
    return master().coreApi().userService();
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkUser> aValues ) {
    String id = aValues.getAsAv( AID_STRID ).asString();
    if( !StridUtils.isValidIdPath( id ) ) {
      return ValidationResult.error( MSG_ERR_LOGIN_NOT_IDPATH );
    }
    IDtoFullObject dtoUser = makeUserDto( aValues, coreApi() );
    return userService().svs().validator().canCreateUser( dtoUser );
  }

  @Override
  protected ISkUser doCreate( IM5Bunch<ISkUser> aValues ) {
    // user creation beacause of password can not be done through LifecycleManager
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkUser> aValues ) {
    String id = aValues.getAsAv( AID_STRID ).asString();
    if( !StridUtils.isValidIdPath( id ) ) {
      return ValidationResult.error( MSG_ERR_LOGIN_NOT_IDPATH );
    }
    IDtoFullObject dtoUser = makeUserDto( aValues, coreApi() );
    return userService().svs().validator().canEditUser( dtoUser, aValues.originalEntity() );
  }

  @Override
  protected ISkUser doEdit( IM5Bunch<ISkUser> aValues ) {
    IDtoFullObject dtoUser = makeUserDto( aValues, coreApi() );
    return userService().editUser( dtoUser );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkUser aEntity ) {
    return userService().svs().validator().canRemoveUser( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkUser aEntity ) {
    userService().removeUser( aEntity.id() );
  }

  @Override
  protected IList<ISkUser> doListEntities() {
    return userService().listUsers();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  static IDtoFullObject makeUserDto( IM5Bunch<ISkUser> aValues, ISkCoreApi aCoreApi ) {
    String id = aValues.getAsAv( AID_STRID ).asString();
    Skid skid = new Skid( ISkUser.CLASS_ID, id );
    DtoFullObject dtoUser = DtoFullObject.createDtoFullObject( skid, aCoreApi );
    dtoUser.attrs().setValue( AID_NAME, aValues.getAsAv( AID_NAME ) );
    dtoUser.attrs().setValue( AID_DESCRIPTION, aValues.getAsAv( AID_DESCRIPTION ) );
    dtoUser.attrs().setValue( ATRID_USER_IS_ENABLED, aValues.getAsAv( ATRID_USER_IS_ENABLED ) );
    dtoUser.attrs().setValue( ATRID_USER_IS_HIDDEN, aValues.getAsAv( ATRID_USER_IS_HIDDEN ) );
    IListEdit<ISkRole> rolesList = (IListEdit<ISkRole>)aValues.getAs( LNKID_USER_ROLES, IList.class );
    ISkidList roleSkids = SkHelperUtils.objsToSkids( rolesList );
    if( roleSkids.isEmpty() ) {
      // Slavage: 16.1.2025
      ((SkidList)roleSkids).add( SKID_ROLE_GUEST );
    }
    dtoUser.links().ensureSkidList( LNKID_USER_ROLES, roleSkids );
    return dtoUser;
  }

}
