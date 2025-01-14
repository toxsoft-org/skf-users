package org.toxsoft.skf.users.gui.km5;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiConstants.*;
import static org.toxsoft.skf.users.gui.ISkUsersGuiSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link IMultiPaneComponent} implementation for users collection viewer/editor.
 *
 * @author Slavage
 */
public class SkAbilityMpc
    extends MultiPaneComponentModown<ISkAbility>
    implements ISkConnected {

  static final ITsNodeKind<ISkAbilityKind> NK_KIND    = new TsNodeKind<>( "LeafKind", ISkAbilityKind.class, true ); //$NON-NLS-1$
  static final ITsNodeKind<ISkAbility>     NK_ABILITY = new TsNodeKind<>( "NodeAbility", ISkAbility.class, false ); //$NON-NLS-1$

  /**
   * Tree maker groups abilities by kinds.
   *
   * @author hazard157
   */
  class TreeMakerByKind
      implements ITsTreeMaker<ISkAbility> {

    // ------------------------------------------------------------------------------------
    // ITsTreeMaker
    //

    private IStringMapEdit<DefaultTsNode<ISkAbilityKind>> makeKindsMap( ITsNode aRootNode ) {
      IStringMapEdit<DefaultTsNode<ISkAbilityKind>> retVal = new StringMap<>();
      IStridablesList<ISkAbilityKind> kinds = skUserServ().abilityManager().listKinds();
      for( ISkAbilityKind kind : kinds ) {
        DefaultTsNode<ISkAbilityKind> kindNode = new DefaultTsNode<>( NK_KIND, aRootNode, kind );
        // Seting kind name and icon.
        kindNode.setName( kind.attrs().getStr( IM5Constants.FID_NAME ) );
        kindNode.setIconId( ICONID_KIND );
        retVal.put( kind.id(), kindNode );
      }
      return retVal;
    }

    // private IStringMapEdit<DefaultTsNode<ISkAbilityKind>> makeKindsMap( ITsNode aRootNode ) {
    // IStringMapEdit<DefaultTsNode<ISkAbilityKind>> retVal = new StringMap<>();
    // IStridablesList<ISkAbility> abilities = skUserServ().abilityManager().listAbilities();
    // for( ISkAbility ability : abilities ) {
    // ISkAbilityKind kind = ability.kind();
    // DefaultTsNode<ISkAbilityKind> kindNode = new DefaultTsNode<>( NK_KIND, aRootNode, kind );
    // // Seting kind name and icon.
    // kindNode.setName( kind.attrs().getStr( IM5Constants.FID_NAME ) );
    // kindNode.setIconId( ICONID_KIND );
    // retVal.put( kind.id(), kindNode );
    // }
    // return retVal;
    // }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISkAbility> aAbilities ) {
      IStringMapEdit<DefaultTsNode<ISkAbilityKind>> roots = makeKindsMap( aRootNode );
      for( ISkAbility ability : aAbilities ) {
        ISkAbilityKind kind = ability.kind();
        DefaultTsNode<ISkAbilityKind> kindNode = roots.findByKey( kind.id() );
        DefaultTsNode<ISkAbility> abilityNode = new DefaultTsNode<>( NK_ABILITY, kindNode, ability );
        // Seting kind name and icon.
        abilityNode.setName( ability.attrs().getStr( IM5Constants.FID_NAME ) );
        abilityNode.setIconId( ICONID_ABILITY );
        kindNode.addNode( abilityNode );
      }
      return (IList)roots.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_ABILITY;
    }

  }

  /**
   * Creates instance to edit entities.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public SkAbilityMpc( ITsGuiContext aContext, IM5Model<ISkAbility> aModel, IM5ItemsProvider<ISkAbility> aItemsProvider,
      IM5LifecycleManager<ISkAbility> aLifecycleManager ) {
    super( new M5TreeViewer<>( aContext, aModel, OPDEF_IS_SUPPORTS_CHECKS.getValue( aContext.params() ).asBool() ) );
    setItemProvider( aItemsProvider );
    setLifecycleManager( aLifecycleManager );
    //
    TreeModeInfo<ISkAbility> tmiByKide = new TreeModeInfo<>( "ByKind", //$NON-NLS-1$
        STR_N_TMI_BY_KINDS, STR_D_TMI_BY_KINDS, ICONID_KINDS_LIST, new TreeMakerByKind() );
    treeModeManager().addTreeMode( tmiByKide );
    // Default value is tree view.
    treeModeManager().setCurrentMode( "ByKind" ); //$NON-NLS-1$

  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return model().domain().tsContext().get( ISkConnection.class );
  }

}
