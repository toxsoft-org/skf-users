package org.toxsoft.skf.users.skide.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.users.skide.ISkfUsersSkideSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.api.users.ISkUserServiceHardConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.users.skide.main.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitUsers}.
 *
 * @author hazard157
 * @author dima
 */
public class TaskUsersUpload
    extends AbstractSkideUnitTaskSync {

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskUsersUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), IStridablesList.EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitTaskSync
  //

  @SuppressWarnings( "boxing" )
  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    ISkUserService srcUserService = coreApi().userService();
    ISkConnection destConn = UploadToServerTaskProcessor.REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    ISkUserService destUserService = destConn.coreApi().userService();
    // upload users and roles from SkIDE
    IStridablesList<ISkUser> srcUsersList = srcUserService.listUsers();
    for( ISkUser srcUser : srcUsersList ) {
      ISkUser destUser = destUserService.findUser( srcUser.strid() );
      if( destUser != null ) {
        IDtoFullObject srcDtoUser = makeUserDto( srcUser, coreApi() );
        destUser = destUserService.editUser( srcDtoUser );
      }
      else {
        IDtoFullObject srcDtoUser = makeUserDto( srcUser, coreApi() );
        // FIXME get actual password
        destUser = destUserService.createUser( srcDtoUser, "1" );
      }
    }
    IStridablesList<ISkRole> srcRolesList = srcUserService.listRoles();
    for( ISkRole srcRole : srcRolesList ) {
      ISkRole userRole = srcUserService.findRole( srcRole.strid() );
      DtoObject srcDtoRole = DtoObject.createDtoObject( userRole.skid(), coreApi() );
      destUserService.defineRole( srcDtoRole );
    }
    ValidationResult vr = ValidationResult.info( FMT_INFO_USERS_UPLOADED, srcUsersList.size(), srcRolesList.size() );
    lop.finished( vr );
    REFDEF_OUT_TASK_RESULT.setRef( aOutput, vr );
  }

  static IDtoFullObject makeUserDto( ISkUser aOriginUser, ISkCoreApi aCoreApi ) {
    String id = aOriginUser.id();
    Skid skid = new Skid( ISkUser.CLASS_ID, id );
    DtoFullObject dtoUser = DtoFullObject.createDtoFullObject( skid, aCoreApi );
    dtoUser.attrs().setValue( AID_NAME, aOriginUser.attrs().getValue( AID_NAME ) );
    dtoUser.attrs().setValue( AID_DESCRIPTION, aOriginUser.attrs().getValue( AID_DESCRIPTION ) );
    dtoUser.attrs().setValue( ATRID_USER_IS_ENABLED, aOriginUser.attrs().getValue( ATRID_USER_IS_ENABLED ) );
    dtoUser.attrs().setValue( ATRID_USER_IS_HIDDEN, aOriginUser.attrs().getValue( ATRID_USER_IS_HIDDEN ) );
    IList<ISkRole> rolesList = aOriginUser.getLinkObjs( LNKID_USER_ROLES );
    ISkidList roleSkids = SkHelperUtils.objsToSkids( rolesList );
    dtoUser.links().ensureSkidList( LNKID_USER_ROLES, roleSkids );
    return dtoUser;
  }

}
