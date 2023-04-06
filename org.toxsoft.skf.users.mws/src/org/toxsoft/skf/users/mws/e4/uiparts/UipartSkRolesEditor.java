package org.toxsoft.skf.users.mws.e4.uiparts;

import javax.inject.Inject;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsSelectionChangeListener;
import org.toxsoft.core.tsgui.mws.services.currentity.ICurrentEntityChangeListener;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.skf.users.gui.panels.PanelSkRolesEditor;
import org.toxsoft.skf.users.mws.e4.service.ICurrentUsersMwsSkRoleService;
import org.toxsoft.uskat.core.api.users.ISkRole;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.e4.uiparts.SkMwsAbstractPart;

/**
 * Uipart contains {@link PanelSkRolesEditor} and respects {@link ICurrentUsersMwsSkRoleService#current()}.
 * <p>
 * By default sets {@link PanelSkRolesEditor#getUsedConnectionId()} to <code>null</code> using Sk-connection
 * {@link ISkConnectionSupplier#defConn()} for roles management.
 * <p>
 * TODO add an ability to specify {@link IdChain} of used connection
 *
 * @author hazard157
 */
public class UipartSkRolesEditor
    extends SkMwsAbstractPart {

  /**
   * Changes selection in panel when {@link ICurrentUsersMwsSkRoleService#current()} changes.
   */
  private final ICurrentEntityChangeListener<ISkRole> currentUsersMwsSkRoleServiceListener =
      aCurrent -> this.rolesEditor.setSelectedItem( aCurrent );

  /**
   * Changes {@link ICurrentUsersMwsSkRoleService#current()} when selection in panel changes.
   */
  private final ITsSelectionChangeListener<ISkRole> panelSelectionChangeListener =
      ( aSource, aSelectedItem ) -> this.currentUsersMwsSkRoleService.setCurrent( aSelectedItem );

  @Inject
  ICurrentUsersMwsSkRoleService currentUsersMwsSkRoleService;

  PanelSkRolesEditor rolesEditor;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    rolesEditor = new PanelSkRolesEditor( ctx, null );
    rolesEditor.createControl( aParent );
    rolesEditor.addTsSelectionListener( panelSelectionChangeListener );
    currentUsersMwsSkRoleService.addCurrentEntityChangeListener( currentUsersMwsSkRoleServiceListener );
  }

}
