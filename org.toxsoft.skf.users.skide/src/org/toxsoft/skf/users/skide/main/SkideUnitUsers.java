package org.toxsoft.skf.users.skide.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;
import static org.toxsoft.skf.users.skide.ISkfUsersSkideSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.users.skide.tasks.upload.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * SkiDE unit: USkat users and roles management.
 *
 * @author hazard157
 */
public class SkideUnitUsers
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.users"; //$NON-NLS-1$

  SkideUnitUsers( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_USERS_MANAGEMENT, //
        TSID_DESCRIPTION, STR_SKIDE_USERS_MANAGEMENT_D, //
        OPDEF_SKIDE_UNIT_CATEGORY, UCATEGID_ADMINISTRATION, //
        TSID_ICON_ID, ICONID_USER_EDITOR //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitPanelUsers( aContext, this );
  }

  @Override
  protected void doFillTasks( IStringMapEdit<AbstractSkideUnitTask> aTaskRunnersMap ) {
    AbstractSkideUnitTask task = new TaskUsersUpload( this );
    aTaskRunnersMap.put( task.taskInfo().id(), task );
  }

}
