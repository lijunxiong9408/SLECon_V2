//package vecspectra.inspect.door;
//import java.lang.Thread.UncaughtExceptionHandler;
//
//import javax.net.ssl.SSLEngineResult.Status;
//import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
//
//import logic.connection.LiftConnectionBean;
//import logic.ocs.CheckedException;
//import logic.ocs.ExceptionHandler;
//import logic.ocs.HasRole;
//import logic.ocs.LiftUpdateThread.NoPermissionException;
//import logic.ocs.OrdinaryHandler;
//import logic.ocs.UpdateThreadHandler;
//import logic.util.VecToolBox;
//import slecon.component.NoPermissionPanel;
//import slecon.inspect.iostatus.OnOffStatus;
//
//
//
//
//public class DoorPageHandler extends UpdateThreadHandler {
//    private LiftConnectionBean connBean;
//    private Door               page;
//
//
//
//
//    public DoorPageHandler ( Door page, LiftConnectionBean connBean ) {
//        this.connBean = connBean;
//        this.page     = page;
//    }
//
//
//    @ExceptionHandler
//    public final class ExceptionRunnable implements UncaughtExceptionHandler {
//        @Override
//        public void uncaughtException ( Thread t, Throwable e ) {
//            if ( e instanceof NoPermissionException ) {
//                boolean result = VecToolBox.showLoginPanel( connBean );
//                if ( result )
//                    page.getWorkspace().restart();
//                else {
//                    JPanel panel = new NoPermissionPanel();
//                    page.getWorkspace().setMainPanel( panel );
//                    System.out.println( panel.isOpaque() );
//                }
//            }
//        }
//    }
//
//
//
//
//    @OrdinaryHandler
//    @HasRole( { "inspect.ocs.read" } )
//    public class OrdinaryRunnable implements Runnable {
//        @Override
//        public void run () {
//            DCSAgent dcsAgent = new DCSAgent( connBean.getIp(), connBean.getPort() );
//            OCSAgent ocsAgent = new OCSAgent( connBean.getIp(), connBean.getPort() );
//            try {
//                if ( ! dcsAgent.syncInfo() )
//                    throw new CheckedException( "sync not ok" );
//                if ( ! ocsAgent.syncSysInfoData() )
//                    throw new CheckedException( "sync not ok" );
//            } catch ( XmlRpcException e1 ) {
//                throw new RuntimeException( "xml-rpc", e1 );
//            }
//
//            SysInfo                  sysInfo = ocsAgent.getSysInfo();
//            final Door.DCSStatusBean dcsBean = new Door.DCSStatusBean();
//            final Door.DoorBean      front   = new Door.DoorBean();
//            dcsBean.setFan( toOnOffStatus( status.getFan( true ) ) );
//            dcsBean.setLight( toOnOffStatus( status.getLight( true ) ) );
//
//            DCSInfo dcsinfo = dcsAgent.getDCSInfo();
//            dcsBean.setAdvbk( toOnOffStatus( dcsinfo.getADVBK() ) );
//
//            // TODO By Eric
//            // LDZ (InputSource.LDZ), UDZ (InputSource.UDZ), LSL (InputSource.LSL) and USL (InputSource.USL)
//            // INS (InputSource.INS), INSUP (InputSource.INSUP), INSDOWN (InputSource.INSDOWN) are now from Crossbar.
//            // INS, INSUP, INSDOWN have no Door Position!!
//            // Parser_McsConfig.getCrossbar()
//            // Parser_Status.getMcsIO()
//            
//            // TODO please see UI  @param:[which door]
//            dcsBean.setIns( toOnOffStatus( dcsinfo.getINS( logic.CabinAndDoor.Position.FRONT ) ) );
//            dcsBean.setInsup( toOnOffStatus( dcsinfo.getINSUP( logic.CabinAndDoor.Position.FRONT ) ) );
//            dcsBean.setInsdown( toOnOffStatus( dcsinfo.getINSDOWN( logic.CabinAndDoor.Position.FRONT ) ) );
//            dcsBean.setLdz( toOnOffStatus( dcsinfo.getLDZ() ) );
//            dcsBean.setUdz( toOnOffStatus( dcsinfo.getUDZ() ) );
//            dcsBean.setLsl( toOnOffStatus( dcsinfo.getLSL() ) );
//            dcsBean.setUsl( toOnOffStatus( dcsinfo.getUSL() ) );
//            front.setDoorStatus( status.getDoorStatus( true ) );
//            front.setDcl( toOnOffStatus( status.getDCL( true ) ) );
//            front.setDol( toOnOffStatus( status.getDOL( true ) ) );
//            front.setDor( toOnOffStatus( status.getDOR( true ) ) );
//            front.setSgs( toOnOffStatus( status.getSGS( true ) ) );
//            front.setEdp( toOnOffStatus( status.getEDP( true ) ) );
//            SwingUtilities.invokeLater( new Runnable() {
//                @Override
//                public void run () {
//                    page.setDCSStatusBean( dcsBean );
//                    page.setFrontDoorBean( front );
//                }
//            } );
//        }
//
//
//        private OnOffStatus toOnOffStatus ( Boolean val ) {
//            return val != null && val
//                   ? OnOffStatus.ON
//                   : OnOffStatus.OFF;
//        }
//    }
//}
