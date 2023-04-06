package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.IM5Bunch;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.skid.ISkidList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsUnsupportedFeatureRtException;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.objserv.IDtoFullObject;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.km5.KM5LifecycleManagerBasic;
import org.toxsoft.uskat.core.impl.dto.DtoFullObject;
import org.toxsoft.uskat.core.utils.SkHelperUtils;

/**
 * Lifecylce manager for {@link SkUserM5Model}.
 *
 * @author hazard157
 * @author dima
 */
public class SkUserM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkUser, ISkConnection> {

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
    IList<ISkRole> rolesList = aValues.getAs( LNKID_USER_ROLES, IList.class );
    ISkidList roleSkids = SkHelperUtils.objsToSkids( rolesList );
    dtoUser.links().ensureSkidList( LNKID_USER_ROLES, roleSkids );
    return dtoUser;
  }

}
