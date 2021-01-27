package comm;
import static logic.util.SiteManagement.MON_MGR;

import comm.agent.AgentPacket;
import comm.util.Endian;




/**
 * Module parser.
 */
public class Parser_Module {
    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * Module data.
     */
    private final byte[] module;
    
    /**
     * The total size of module data.
     */
    public static final int SIZE = 17445;
    
    /**
     * Module data.
     */
    public final Insp insp = new Insp();
    public final Isc  isc  = new Isc();
    public final Aro  aro  = new Aro();
    public final Att  att  = new Att();
    public final Fro  fro  = new Fro();
    public final Feo  feo  = new Feo();
    public final Pak  pak  = new Pak();
    public final Olp  olp  = new Olp();
    public final Ngp  ngp  = new Ngp();
    public final Epo  epo  = new Epo();
    public final Acc  acc  = new Acc();
    public final Cco  cco  = new Cco();
    public final Dcs  dcs  = new Dcs();
    public final Tdeo  tdeo  = new Tdeo();
    public final RDR  rdr  = new RDR();
    public final RDS  rds  = new RDS();
    public final VES  ves  = new VES();
    public final EQO  eqo  = new EQO();
    public final FLO  flo  = new FLO();
    public final P2O  p2o  = new P2O();
    public final DHO  dho  = new DHO();
    public final EPB  epb  = new EPB();
    public final NONSTOP nonstop = new NONSTOP();
    public final ACDO  acdo = new ACDO();
    public final MOH  moh = new MOH();

    /**
     * Module parser.
     * @param hostname  It specifies the host name of agent.
     * @param port      It specifies the host port of agent.
     */
    public Parser_Module ( String hostname, int port ) {
        this.agent  = MON_MGR.getAgent( hostname, port );
        this.module = this.agent.module;
    }


    /**
     * Send local copy of Module data to OCS.
     */
    public void commit () {
        byte[] ret = new byte[ SIZE + 1 ];
        synchronized ( module ) {
            System.arraycopy( this.module, 0, ret, 1, SIZE );
        }
        ret[ 0 ] = AgentPacket.PACKET_MODULE;
        this.agent.send( ret );
    }


    /**
     * Inspection.
     */
    public class Insp {
        private static final int OFFSET = 0;
        
        public byte getCar_message () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 0 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 0 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 1 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = hall_message;
            }
        }
        
        
        public byte getFault_Car_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 2 ];
            }
        }


        public void setFault_Car_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = car_message;
            }
        }
        
        
        public byte getFault_Hall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 3 ];
            }
        }


        public void setFault_Hall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = hall_message;
            }
        }
        
        
        public short getFault_sign_keep_timer() {
        	synchronized ( module ) {
        		return Endian.getShort(module, 4);
        	}
        }
        
        public void setFault_sign_keep_timer(short timer) {
        	synchronized ( module ) {
        		System.arraycopy(Endian.getShortByteArray(timer), 0, module, OFFSET + 4, 2);
        	}
        }
    }
    
    
    /**
     * Independent service.
     */
    public class Isc {
        private static final int OFFSET = 8;
    
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ] ;
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ] ;
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }
        
        public byte getRunStrategy () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 3 ];
            }
        }

        public void setRunStrategy ( byte strategy ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = strategy;
            }
        }
    }
    
    
    /**
     * Auto return operation.
     */
    public class Aro {
        private static final int OFFSET = 16;
    
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_group_auto_return () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_group_auto_return ( boolean enable_group_auto_return ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_group_auto_return ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_energy_saving_on_car () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_car ( boolean enable_energy_saving_on_car ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_car ? 1 : 0, 2, 2 );
            }
        }


        public boolean isEnable_energy_saving_on_hall () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_hall ( boolean enable_energy_saving_on_hall ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_hall ? 1 : 0, 3, 3 );
            }
        }


        public boolean isEnabled_open_door_button_led () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setEnabled_open_door_button_led ( boolean enabled_open_door_button_led ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled_open_door_button_led ? 1 : 0, 4, 4 );
            }
        }


        public boolean isDisable_cabin_fan () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 5, 5 ) != 0;
            }
        }


        public void setDisable_cabin_fan ( boolean disable_cabin_fan ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_fan ? 1 : 0, 5, 5 );
            }
        }


        public boolean isDisable_cabin_light () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 6, 6 ) != 0;
            }
        }


        public void setDisable_cabin_light ( boolean disable_cabin_light ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_light ? 1 : 0, 6, 6 );
            }
        }


        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 1 ];
            }
        }


        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )( return_floor & 0xFF );
            }
        }


        public int getActivation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 2 ) / 1000;
            }
        }


        public void setActivation_time ( int activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( activation_time * 1000 ), 0, module, OFFSET + 2, 4 );
            }
        }


        public int getEnable_energy_saving_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 6 ) / 1000;
            }
        }


        public void setEnable_energy_saving_activation_time ( int enable_energy_saving_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( enable_energy_saving_activation_time * 1000 ), 0, module, OFFSET + 6, 4 );
            }
        }


        public int getOpen_door_button_led_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 10 ) / 1000;
            }
        }


        public void setOpen_door_button_led_activation_time ( int open_door_button_led_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( open_door_button_led_activation_time * 1000 ), 0, module, OFFSET + 10, 4 );
            }
        }


        public int getDisable_cabin_fan_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 14 ) / 1000;
            }
        }


        public void setDisable_cabin_fan_activation_time ( int disable_cabin_fan_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_fan_activation_time * 1000 ), 0, module, OFFSET + 14, 4 );
            }
        }


        public int getDisable_cabin_light_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 18 ) / 1000;
            }
        }


        public void setDisable_cabin_light_activation_time ( int disable_cabin_light_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_light_activation_time * 1000 ), 0, module, OFFSET + 18, 4 );
            }
        }


        public byte[] getFloor_list () {
            synchronized ( module ) {
                byte[] b = new byte[ 16 ];
                System.arraycopy( module, OFFSET + 22, b, 0, 16 );
                return b;
            }
        }


        public void setFloor_list ( byte[] b ) {
            synchronized ( module ) {
                System.arraycopy( b, 0, module, OFFSET + 22, 16 );
            }
        }
        
        
        public byte getPriority_len () {
            synchronized ( module ) {
                return module[ OFFSET + 38 ];
            }
        }
        
        
        public void setPriority_len ( byte l ) {
            synchronized ( module ) {
                module[ OFFSET + 38 ] = l;
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 39 ];
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 39 ] = led_Behavior;
            }
        }
    }
    
    
    /**
     * Attendant service.
     */
    public class Att {
        private static final int OFFSET = 80;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnabled_front_buzzer_on_hall_call () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnabled_front_buzzer_on_hall_call ( boolean enabled_front_buzzer_on_hall_call ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled_front_buzzer_on_hall_call ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnabled_rear_buzzer_on_hall_call () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnabled_rear_buzzer_on_hall_call ( boolean enabled_rear_buzzer_on_hall_call ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled_rear_buzzer_on_hall_call ? 1 : 0, 2, 2 );
            }
        }


        public byte getCar_led_behavior () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 0 ], 3, 4 );
            }
        }


        public void setCar_led_behavior ( byte car_led_behavior ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], car_led_behavior, 3, 4 );
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 1 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }

    }
    
    
    /**
     * Fire emergency return operation.
     */
    public class Fro {
        private static final int OFFSET = 88;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_energy_saving_on_car () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_car ( boolean enable_energy_saving_on_car ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_car ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_energy_saving_on_hall () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_hall ( boolean enable_energy_saving_on_hall ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_hall ? 1 : 0, 2, 2 );
            }
        }


        public boolean isDisable_cabin_fan () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setDisable_cabin_fan ( boolean disable_cabin_fan ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_fan ? 1 : 0, 3, 3 );
            }
        }


        public boolean isDisable_cabin_light () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setDisable_cabin_light ( boolean disable_cabin_light ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_light ? 1 : 0, 4, 4 );
            }
        }
        
        public boolean isEnable_edp () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 5, 5 ) != 0;
            }
        }
        
        public void setEnable_edp ( boolean enable_edp ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_edp ? 1 : 0, 5, 5 );
            }
        }
        
        public boolean isEnable_sgs () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 6, 6 ) != 0;
            }
        }
        
        public void setEnable_sgs ( boolean enable_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_sgs ? 1 : 0, 6, 6 );
            }
        }

        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ] ;
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }


        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }


        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )return_floor;
            }
        }


        public int getEnable_energy_saving_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 4 ) / 1000;
            }
        }


        public void setEnable_energy_saving_activation_time ( int enable_energy_saving_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( enable_energy_saving_activation_time * 1000 ), 0, module, OFFSET + 4, 4 );
            }
        }


        public int getDisable_cabin_fan_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 8 ) / 1000;
            }
        }


        public void setDisable_cabin_fan_activation_time ( int disable_cabin_fan_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_fan_activation_time * 1000 ), 0, module, OFFSET + 8, 4 );
            }
        }


        public int getDisable_cabin_light_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 12 ) / 1000;
            }
        }


        public void setDisable_cabin_light_activation_time ( int disable_cabin_light_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_light_activation_time * 1000 ), 0, module, OFFSET + 12, 4 );
            }
        }
        
        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 0, 0 ) != 0;
            }
        }
        
        public void setEnable_front_buzzer ( boolean enable_front_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_front_sgs ? 1 : 0, 0, 0 );
            }
        }
        
        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 1, 1 ) != 0;
            }
        }
        
        public void setEnable_rear_buzzer ( boolean enable_rear_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_rear_sgs ? 1 : 0, 1, 1 );
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 17 ];
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 17 ] = led_Behavior;
            }
        }
        
        public byte getRunStrategy () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 18 ];
            }
        }

        public void setRunStrategy ( byte strategy ) {
            synchronized ( module ) {
            	module[ OFFSET + 18 ] = strategy;
            }
        }
        
        public int getDoor_Close_timer () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 19 ) / 1000;
            }
        }

        public void setDoor_Close_timer ( int door_close_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_time * 1000 ), 0, module, OFFSET + 19, 4 );
            }
        }
    }
    
    /**
     * Fireman's emergency operation.
     */
    public class Feo {
        private static final int OFFSET = 120;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public int getStrategy () {
            synchronized ( module ) {
                return ( int )Endian.getBitsValue( module[ OFFSET + 0 ], 1, 2 );
            }
        }


        public void setStrategy ( int strategy ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], strategy, 1, 2 );
            }
        }

        public boolean isEnable_edp () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }
        
        public void setEnable_edp ( boolean enable_edp ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_edp ? 1 : 0, 3, 3 );
            }
        }
        
        public boolean isEnable_sgs () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }
        
        public void setEnable_sgs ( boolean enable_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_sgs ? 1 : 0, 4, 4 );
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }


        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }


        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )return_floor;
            }
        }
    }
    
    
    /**
     * Parking.
     */
    public class Pak {
        private static final int OFFSET = 128;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_energy_saving_on_car () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_car ( boolean enable_energy_saving_on_car ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_car ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_energy_saving_on_hall () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_hall ( boolean enable_energy_saving_on_hall ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_hall ? 1 : 0, 2, 2 );
            }
        }


        public boolean isEnabled_open_door_button_led () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setEnabled_open_door_button_led ( boolean enabled_open_door_button_led ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled_open_door_button_led ? 1 : 0, 3, 3 );
            }
        }


        public boolean isDisable_cabin_fan () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setDisable_cabin_fan ( boolean disable_cabin_fan ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_fan ? 1 : 0, 4, 4 );
            }
        }


        public boolean isDisable_cabin_light () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 5, 5 ) != 0;
            }
        }


        public void setDisable_cabin_light ( boolean disable_cabin_light ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_light ? 1 : 0, 5, 5 );
            }
        }


        public boolean isClear_car_call () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 6, 6 ) != 0;
            }
        }


        public void setClear_car_call ( boolean clear_car_call ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], clear_car_call ? 1 : 0 , 6, 6);
            }
        }


        public boolean isReject_new_car_call () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 7, 7 ) != 0;
            }
        }


        public void setReject_new_car_call ( boolean reject_new_car_call ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], reject_new_car_call ? 1 : 0, 7, 7 );
            }
        }


        public boolean isOpen_door_before_go_to_return_floor () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 1 ], 0, 0 ) != 0;
            }
        }


        public void setOpen_door_before_go_to_return_floor ( boolean open_door_before_go_to_return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], open_door_before_go_to_return_floor ? 1 : 0, 0, 0 );
            }
        }


        public boolean isOpen_door_once_arrival_return_floor () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 1 ], 1, 1 ) != 0;
            }
        }


        public void setOpen_door_once_arrival_return_floor ( boolean open_door_once_arrival_return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], open_door_once_arrival_return_floor ? 1 : 0, 1, 1 );
            }
        }
        
        public boolean isEnable_edp () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 1 ], 2, 2 ) != 0;
            }
        }
        
        public void setEnable_edp ( boolean enable_edp ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], enable_edp ? 1 : 0, 2, 2 );
            }
        }
        
        public boolean isEnable_sgs () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 1 ], 3, 3 ) != 0;
            }
        }
        
        public void setEnable_sgs ( boolean enable_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], enable_sgs ? 1 : 0, 3, 3 );
            }
        }

        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = car_message;
            }
        }
        
        

        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 3 ] ;
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = hall_message;
            }
        }


        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 4 ];
            }
        }


        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 4 ] = ( byte )return_floor;
            }
        }


        public int getEnable_energy_saving_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 5 ) / 1000;
            }
        }


        public void setEnable_energy_saving_activation_time ( int enable_energy_saving_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( enable_energy_saving_activation_time * 1000 ), 0, module, OFFSET + 5, 4 );
            }
        }


        public int getOpen_door_button_led_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 9 ) / 1000;
            }
        }


        public void setOpen_door_button_led_activation_time ( int open_door_button_led_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( open_door_button_led_activation_time * 1000 ), 0, module, OFFSET + 9, 4 );
            }
        }


        public int getDisable_cabin_fan_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 13 ) / 1000;
            }
        }


        public void setDisable_cabin_fan_activation_time ( int disable_cabin_fan_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_fan_activation_time * 1000 ), 0, module, OFFSET + 13, 4 );
            }
        }


        public int getDisable_cabin_light_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 17 ) / 1000;
            }
        }


        public void setDisable_cabin_light_activation_time ( int disable_cabin_light_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_light_activation_time * 1000 ), 0, module, OFFSET + 17, 4 );
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 21 ];
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 21 ] = led_Behavior;
            }
        }
    }
    
    
    /**
     * Overload.
     */
    public class Olp {
        private static final int OFFSET = 160;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_front_buzzer ( boolean enable_front_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_front_buzzer ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_rear_buzzer ( boolean enable_rear_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_rear_buzzer ? 1 : 0, 2, 2 );
            }
        }


        public boolean isClear_car_calls () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setClear_car_calls ( boolean clear_car_calls ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], clear_car_calls ? 1 : 0, 3, 3 );
            }
        }


        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }


        public int getPercentage () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }


        public void setPercentage ( int percentage ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )percentage;
            }
        }


        public int getClear_car_calls_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 4 ) / 1000;
            }
        }


        public void setClear_car_calls_activation_time ( int clear_car_calls_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( clear_car_calls_activation_time * 1000 ), 0, module, OFFSET + 4, 4 );
            }
        }
    }
    
    /**
     * Fault state group suspension.
     */
    public class Ngp {
        private static final int OFFSET = 168;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public byte getScheme () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 );
            }
        }


        public void setScheme ( byte scheme ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], scheme, 1, 1 );
            }
        }


        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_front_buzzer ( boolean enable_front_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_front_buzzer ? 1 : 0, 2, 2 );
            }
        }


        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setEnable_rear_buzzer ( boolean enable_rear_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_rear_buzzer ? 1 : 0, 3, 3 );
            }
        }


        public int getActivation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 1 ) / 1000;
            }
        }


        public void setActivation_time ( int activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( activation_time * 1000 ), 0, module, OFFSET + 1, 4 );
            }
        }
    }
    
    
    /**
     * Emergency power operation.
     */
    public class Epo {
        private static final int OFFSET = 176;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }

        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 0 ], 1, 2 );
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], led_Behavior, 1, 2 );
            }
        }
        
        public boolean isFrontDoorEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }

        public void setFrontDoorEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 3, 3 );
            }
        }
        
        public boolean isRearDoorEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setRearDoorEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 4, 4 );
            }
        }
        
        public byte getScheme () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 0 ], 5, 6 );
            }
        }

        public void setScheme ( byte scheme ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], scheme, 5, 6 );
            }
        }
        
        public byte getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 1 ];
            }
        }

        public void setReturn_floor ( byte return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = return_floor;
            }
        }
        
        public byte getDoorCloseTime () {
            synchronized ( module ) {
                return module[ OFFSET + 2 ];
            }
        }

        public void setDoorCloseTime ( byte times ) {
            synchronized ( module ) {
                module[ OFFSET + 2 ] = times;
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 3 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 4 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 4 ] = hall_message;
            }
        }
        
        public int getAutoRunSelector () {
            synchronized ( module ) {
                return ( int )Endian.getShort( module, OFFSET + 5 );
            }
        }


        public void setAutoRunSelector ( int seletor ) {
            synchronized ( module ) {
            	System.arraycopy(Endian.getIntByteArray(seletor), 0, module, OFFSET + 5, 2);
            }
        }
    }

    /**
     * Access control.
     */
    public class Acc {
        private static final int OFFSET = 184;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isMaintain_car_call () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setMaintain_car_call ( boolean maintain_car_call ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], maintain_car_call ? 1 : 0, 1, 1 );
            }
        }


        public boolean isForce_use_selected_operation_set () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setForce_use_selected_operation_set ( boolean force_use_selected_operation_set ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], force_use_selected_operation_set ? 1 : 0, 2, 2 );
            }
        }
        
        public byte getOperation_set () {
            synchronized ( module ) {
                return module[ OFFSET + 1 ];
            }
        }


        public void setOperation_set ( byte operation_set ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = operation_set;
            }
        }
        
        public byte[][] getAccess_table () {
            synchronized ( module ) {
                byte b[][] = new byte[ 128 ][ 128 ];
                for ( int i = 0, len = 16384 ; i < len ; i++ )
                    b[ i / 128 ][ i % 128 ] = module[ OFFSET + 2 + i ];
                return b;
            }
        }


        public void setAccess_table ( byte table[][] ) {
            synchronized ( module ) {
                for ( int i = 0 ; i < 128 ; i++ )
                    for ( int j = 0 ; j < 128 ; j++ )
                        module[ OFFSET + 2 + i * 128 + j ] = table[ i ][ j ];
            }
        }
    }


    /**
     * Car call option.
     */
    public class Cco {
        private static final int OFFSET = 16584;
        
        public boolean isAnti_nuisance_car_call_operation_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setAnti_nuisance_car_call_operation_enabled ( boolean anti_nuisance_car_call_operation_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], anti_nuisance_car_call_operation_enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isCalls_in_opposite_direction_auto_clear_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setCalls_in_opposite_direction_auto_clear_enabled ( boolean calls_in_opposite_direction_auto_clear_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], calls_in_opposite_direction_auto_clear_enabled ? 1 : 0, 1, 1 );
            }
        }
        
        public boolean isCalls_in_double_click_auto_clear_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setCalls_in_double_click_auto_clear_enabled ( boolean calls_in_double_click_auto_clear_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], calls_in_double_click_auto_clear_enabled ? 1 : 0, 2, 2 );
            }
        }


        public int getPercentage () {
            synchronized ( module ) {
                return module[ OFFSET + 1 ];
            }
        }


        public void setPercentage ( int percentage ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )percentage;
            }
        }


        public int getCar_call_count () {
            synchronized ( module ) {
                return module[ OFFSET + 2 ];
            }
        }


        public void setCar_call_count ( int car_call_count ) {
            synchronized ( module ) {
                module[ OFFSET + 2 ] = ( byte )car_call_count;
            }
        }
        
        public boolean getNearEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 3 ], 0, 0 ) != 0;
            }
        }


        public void setNearEnabled ( boolean enable ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 3 ], enable ? 1 : 0, 0, 0 );
            }
        }
        
        public byte getDirect () {
            synchronized ( module ) {
                return (byte)Endian.getBitsValue( module[ OFFSET + 3 ], 1, 1 );
            }
        }


        public void setDirect ( byte direct ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 3 ], direct , 1, 1 );
            }
        }
        
        public boolean isASPEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 4 ], 0, 0 ) != 0;
            }
        }


        public void setASPEnabled ( boolean enable ) {
            synchronized ( module ) {
                module[ OFFSET + 4 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 4 ], enable ? 1 : 0, 0, 0 );
            }
        }
    }

    
    /**
     * Door control system.
     */
    public class Dcs {
        private static final int OFFSET = 16592;
        
        public boolean isDoor_hold_button_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setDoor_hold_button_enabled ( boolean door_hold_button_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], door_hold_button_enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isIndependent_control_of_car_door_and_landing_door_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setIndependent_control_of_car_door_and_landing_door_enabled (
                boolean independent_control_of_car_door_and_landing_door_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], independent_control_of_car_door_and_landing_door_enabled ? 1 : 0, 1, 1 );
            }
        }
        
        
        public boolean isForced_close_door_enabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setForced_close_door_enabled ( boolean Forced_close_door_enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], Forced_close_door_enabled ? 1 : 0, 2, 2 );
            }
        }


        public int getDoor_opening_timeout () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 1 ) / 1000;
            }
        }


        public void setDoor_opening_timeout ( int door_opening_timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_opening_timeout * 1000 ), 0, module, OFFSET + 1, 4 );
            }
        }


        public int getDoor_opened_timeout () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 5 ) / 1000;
            }
        }


        public void setDoor_opened_timeout ( int door_opened_timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_opened_timeout * 1000 ), 0, module, OFFSET + 5, 4 );
            }
        }


        public int getDoor_closing_timeout () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 9 ) / 1000;
            }
        }


        public void setDoor_closing_timeout ( int door_closing_timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_closing_timeout * 1000 ), 0, module, OFFSET + 9, 4 );
            }
        }


        public int getDoor_closed_timeout () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 13 ) / 1000;
            }
        }


        public void setDoor_closed_timeout ( int door_closed_timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_closed_timeout * 1000 ), 0, module, OFFSET + 13, 4 );
            }
        }


        public int getDoor_close_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 17 ) / 1000;
            }
        }


        public void setDoor_close_time ( int door_close_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_time * 1000 ), 0, module, OFFSET + 17, 4 );
            }
        }


        public int getRetry_count () {
            synchronized ( module ) {
                return module[ OFFSET + 21 ];
            }
        }


        public void setRetry_count ( int retry_count ) {
            synchronized ( module ) {
                module[ OFFSET + 21 ] = ( byte )retry_count;
            }
        }


        public int getActivation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 22 ) / 1000;
            }
        }


        public void setActivation_time ( int activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( activation_time * 1000 ), 0, module, OFFSET + 22, 4 );
            }
        }


        public int getHolding_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 26 ) / 1000;
            }
        }


        public void setHolding_time ( int holding_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( holding_time * 1000 ), 0, module, OFFSET + 26, 4 );
            }
        }


        public int getFront_door_car_calls () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 30 ) / 1000;
            }
        }


        public void setFront_door_car_calls ( int front_door_car_calls ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( front_door_car_calls * 1000 ), 0, module, OFFSET + 30, 4 );
            }
        }


        public int getRear_door_car_calls () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 34 ) / 1000;
            }
        }


        public void setRear_door_car_calls ( int rear_door_car_calls ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( rear_door_car_calls * 1000 ), 0, module, OFFSET + 34, 4 );
            }
        }


        public int getFront_door_hall_calls () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 38 ) / 1000;
            }
        }


        public void setFront_door_hall_calls ( int front_door_hall_calls ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( front_door_hall_calls * 1000 ), 0, module, OFFSET + 38, 4 );
            }
        }


        public int getRear_door_hall_calls () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 42 ) / 1000;
            }
        }


        public void setRear_door_hall_calls ( int rear_door_hall_calls ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( rear_door_hall_calls * 1000 ), 0, module, OFFSET + 42, 4 );
            }
        }
        
        
        public boolean isOLUEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 46 ], 0, 0 ) != 0;
            }
        }


        public void setOLUEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 46 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 46 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        
        public int getOLUExpireDay () {
            synchronized ( module ) {
                return module[ OFFSET + 47 ];
            }
        }


        public void setOLUExpireDay ( int day ) {
            synchronized ( module ) {
                module[ OFFSET + 47 ] = ( byte )day;
            }
        }
        
        
        public byte getOLUCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 48 ];
            }
        }


        public void setOLUCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 48 ] = car_message;
            }
        }


        public byte getOLUHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 49 ];
            }
        }


        public void setOLUHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 49 ] = hall_message;
            }
        }
        
        
        public int getDDOWait_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 50 ) / 1000;
            }
        }


        public void setDDOWait_time ( int timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( timeout * 1000 ), 0, module, OFFSET + 50, 4 );
            }
        }
        
        public int getDisabled_Door_close_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 54 ) / 1000;
            }
        }


        public void setDisabled_Door_close_time ( int door_close_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_time * 1000 ), 0, module, OFFSET + 54, 4 );
            }
        }
        
        public int getDoor_forced_closed_timeout () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 58 ) / 1000;
            }
        }


        public void setDoor_forced_closed_timeout ( int door_forced_closed_timeout ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_forced_closed_timeout * 1000 ), 0, module, OFFSET + 58, 4 );
            }
        }
        
        public int getChange_Station_Retry_count () {
            synchronized ( module ) {
                return module[ OFFSET + 62 ];
            }
        }


        public void setChange_Station_Retry_count ( int retry_count ) {
            synchronized ( module ) {
                module[ OFFSET + 62 ] = ( byte )retry_count;
            }
        }
    }
    
    /**
     * Temperature Detector Emergency Operation.
     */
    public class Tdeo {
    	private static final int OFFSET = 16656;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_energy_saving_on_car () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_car ( boolean enable_energy_saving_on_car ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_car ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_energy_saving_on_hall () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_hall ( boolean enable_energy_saving_on_hall ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_hall ? 1 : 0, 2, 2 );
            }
        }


        public boolean isDisable_cabin_fan () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setDisable_cabin_fan ( boolean disable_cabin_fan ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_fan ? 1 : 0, 3, 3 );
            }
        }


        public boolean isDisable_cabin_light () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setDisable_cabin_light ( boolean disable_cabin_light ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_light ? 1 : 0, 4, 4 );
            }
        }

        public boolean isEnable_edp () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 5, 5 ) != 0;
            }
        }
        
        public void setEnable_edp ( boolean enable_edp ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_edp ? 1 : 0, 5, 5 );
            }
        }
        
        public boolean isEnable_sgs () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 6, 6 ) != 0;
            }
        }
        
        public void setEnable_sgs ( boolean enable_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_sgs ? 1 : 0, 6, 6 );
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }
        

        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }


        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )return_floor;
            }
        }


        public int getEnable_energy_saving_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 4 ) / 1000;
            }
        }


        public void setEnable_energy_saving_activation_time ( int enable_energy_saving_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( enable_energy_saving_activation_time * 1000 ), 0, module, OFFSET + 4, 4 );
            }
        }


        public int getDisable_cabin_fan_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 8 ) / 1000;
            }
        }


        public void setDisable_cabin_fan_activation_time ( int disable_cabin_fan_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_fan_activation_time * 1000 ), 0, module, OFFSET + 8, 4 );
            }
        }


        public int getDisable_cabin_light_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 12 ) / 1000;
            }
        }


        public void setDisable_cabin_light_activation_time ( int disable_cabin_light_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_light_activation_time * 1000 ), 0, module, OFFSET + 12, 4 );
            }
        }
        
        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 0, 0 ) != 0;
            }
        }
        
        public void setEnable_front_buzzer ( boolean enable_front_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_front_sgs ? 1 : 0, 0, 0 );
            }
        }
        
        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 1, 1 ) != 0;
            }
        }
        
        public void setEnable_rear_buzzer ( boolean enable_rear_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_rear_sgs ? 1 : 0, 1, 1 );
            }
        }
    }
    
    /**
     * Random Run Mode.
     */
    public class RDR {
        private static final int OFFSET = 16688 ;
        
        public int getEnable() {
        	synchronized ( module ) {
        		return (int)Endian.getBitsValue( module[ OFFSET ], 0, 0 );
        	}
        }
        
        public void setEnable(int enable) {
        	synchronized ( module ) {
        		module[ OFFSET ] = ( byte )Endian.setBitsValue( module[ OFFSET ], enable == 1 ? 1 : 0, 0, 0 );
        	}        	
        }
        
        public void setUpdate() {
        	synchronized ( module ) {
        		module[ OFFSET ] = ( byte )Endian.setBitsValue( module[ OFFSET ], 1, 1, 1 );
        	}  
        }
        
        public int getRunTimes() {
        	synchronized ( module ) {
        		return Endian.getInt( module, OFFSET + 1 );
        	}
        }
        
        public void setRunTimes(int times) {
        	synchronized ( module ) {
        		System.arraycopy( Endian.getIntByteArray( times ), 0, module, OFFSET + 1, 4 );
        	}        	
        }
        
        public long getInterval_timer() {
        	synchronized ( module ) {
        		return Endian.getInt( module, OFFSET + 5 ) / 1000;
        	}
        }
        
        public void setInterval_timer(int time) {
        	synchronized ( module ) {
        		System.arraycopy( Endian.getIntByteArray( time * 1000 ), 0, module, OFFSET + 5, 4 );
        	}
        }
        
        public byte[] getRunStrategy() {
        	synchronized ( module ) {
                byte b[] = new byte[ 128 ];
                for ( int i = 0; i < 128 ; i++ )
                    b[i] = module[ OFFSET + 9 + i ];
                return b;
            }
        }
        
        public void setRunStrategy(byte run[]) {
        	synchronized ( module ) {
                for ( int i = 0; i < 128 ; i++ )
                	module[ OFFSET + 9 + i ] = run[i];
            }
        }
        
    }
    
    /**
     * Random Stop Mode.
     */
    public class RDS {
        private static final int OFFSET = 16825;
        
        public boolean getEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
        	}
        }
        
        public void setEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 0, 0 );
        	}        	
        }
        
        public boolean getFanOff() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
        	}
        }
        
        public void setFanOff(byte fan) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], fan, 1, 1 );
        	}
        }
        
        public boolean getLightOff() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
        	}
        }
        
        public void setLightOff(byte light) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], light, 2, 2 );
        	}
        }
        
        public int getCloseTime() {
        	synchronized ( module ) {
        		return Endian.getInt(module, OFFSET + 1) / 1000;
        	}
        }
        
        public void setCloseTime(int closeTime) {
        	synchronized ( module ) {
        		System.arraycopy( Endian.getIntByteArray( closeTime * 1000 ), 0, module, OFFSET + 1, 4 );
        	}
        }
        
    }
    
    /**
     * Voice Enable Setting
     */
    public class VES {
        private static final int OFFSET = 16830;
        
        public boolean getDoorOpenEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
        	}
        }
        
        public void setDoorOpenEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 0, 0 );
        	}        	
        }
        
        public boolean getDoorCloseEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1) != 0;
        	}
        }
        
        public void setDoorCloseEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 1, 1 );
        	}        	
        }
        
        public boolean getDirectEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
        	}
        }
        
        public void setDirectEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 2, 2 );
        	}
        }
        
        public boolean getMessageEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
        	}
        }
        
        public void setMessageEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 3, 3 );
        	}
        }

        public boolean getArrivalEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
        	}
        }
        
        public void setArrivalEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 4, 4 );
        	}
        }
        
        public boolean getButtonEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET + 0 ], 5, 5 ) != 0;
        	}
        }
        
        public void setButtonEnable(byte enable) {
        	synchronized ( module ) {
        		module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable, 5, 5 );
        	}
        }
       

        public short getHallUpVoice() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 1 ], 0, 3);
        	}
        }
        
        public void setHallUpVoice(byte voice) {
        	synchronized ( module ) {
        		module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], voice, 0, 3 );
        	}
        }
        
        public short getHallDownVoice() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 1 ], 4, 7);
        	}
        }
        
        public void setHallDownVoice(byte voice) {
        	synchronized ( module ) {
        		module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], voice, 4, 7 );
        	}
        }
        
        public short getCarUpVoice() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 2 ], 0, 3);
        	}
        }
        
        public void setCarUpVoice(byte voice) {
        	synchronized ( module ) {
        		module[ OFFSET + 2 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 2 ], voice, 0, 3 );
        	}
        }
        
        public short getCarDownVoice() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 2 ], 4, 7);
        	}
        }
        
        public void setCarDownVoice(byte voice) {
        	synchronized ( module ) {
        		module[ OFFSET + 2 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 2 ], voice, 4, 7 );
        	}
        }
        
        public short getLevel_Car_A() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 10 ], 0, 3);
        	}
        }
        
        public void setLevel_Car_A(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 10 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 10 ], level, 0, 3 );
        	}
        }
        
        public short getLevel_Car_B() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 10 ], 4, 7);
        	}
        }
        
        public void setLevel_Car_B(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 10 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 10 ], level, 4, 7 );
        	}
        }
        
        public short getLevel_Car_C() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 11 ], 0, 3);
        	}
        }
        
        public void setLevel_Car_C(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 11 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 11 ], level, 0, 3 );
        	}
        }
        
        public short getLevel_Car_D() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 11 ], 4, 7);
        	}
        }
        
        public void setLevel_Car_D(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 11 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 11 ], level, 4, 7 );
        	}
        }
        
        public short getLevel_Hall_A() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 12 ], 0, 3);
        	}
        }
        
        public void setLevel_Hall_A(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 12 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 12 ], level, 0, 3 );
        	}
        }
        
        public short getLevel_Hall_B() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 12 ], 4, 7);
        	}
        }
        
        public void setLevel_Hall_B(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 12 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 12 ], level, 4, 7 );
        	}
        }
        
        public short getLevel_Hall_C() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 13 ], 0, 3);
        	}
        }
        
        public void setLevel_Hall_C(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 13 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 13 ], level, 0, 3 );
        	}
        }
        
        public short getLevel_Hall_D() {
        	synchronized ( module ) {
        		return (short)Endian.getBitsValue(module[ OFFSET + 13 ], 4, 7);
        	}
        }
        
        public void setLevel_Hall_D(byte level) {
        	synchronized ( module ) {
        		module[ OFFSET + 13 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 13 ], level, 4, 7 );
        	}
        }
    }
    
    /**
     * EarthQuake Operation Setting
     */
    public class EQO{
    	private static final int OFFSET = 16850;
    	
    	public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }

        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 0 ], 1, 2 );
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], led_Behavior, 1, 2 );
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }

        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }

        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }

        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }
        
        public int getDoor_close_timer () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 3 ) / 1000;
            }
        }

        public void setDoor_close_timer ( int door_close_timer ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_timer * 1000 ), 0, module, OFFSET + 3, 4 );
            }
        }
        
        public boolean isFrontBuzzerEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 7 ], 0, 0 ) != 0;
            }
        }

        public void setFrontBuzzerEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 7 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 7 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public boolean isRearBuzzerEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 7 ], 1, 1 ) != 0;
            }
        }

        public void setRearBuzzerEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 7 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 7 ], enabled ? 1 : 0, 1, 1 );
            }
        }
    }
    
    public byte[] getByteArray () {
        byte[] ret;
        synchronized ( module ) {
            ret = new byte[ module.length ];
            System.arraycopy( module, 0, ret, 0, ret.length );
        }
        return ret;
    }
    
    
    public void setByteArray ( byte[] b ) {
        synchronized ( module ) {
            System.arraycopy( b, 0, module, 0, b.length );
        }
    }
    
    /**
     * Full Load.
     */
    public class FLO {
        private static final int OFFSET = 16859;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_front_buzzer ( boolean enable_front_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_front_buzzer ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_rear_buzzer ( boolean enable_rear_buzzer ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_rear_buzzer ? 1 : 0, 2, 2 );
            }
        }

        public byte getCar_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 1 ];
            }
        }

        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }

        public byte getHall_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 2 ] ;
            }
        }

        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }

        public int getPercentage () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }

        public void setPercentage ( int percentage ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )percentage;
            }
        }
    }
    /**
     * Point to Operation.
     * */
    public class P2O {
        private static final int OFFSET = 16863 ;
        
        public Boolean getEnable() {
        	synchronized ( module ) {
        		return Endian.getBitsValue( module[ OFFSET ], 0, 0 ) == 1?true :false;
        	}
        }
        
        public void setEnable(Boolean enable) {
        	synchronized ( module ) {
        		module[ OFFSET ] = ( byte )Endian.setBitsValue( module[ OFFSET ], enable? 1 : 0, 0, 0 );
        	}        	
        }
        
        public byte[] getStrategy() {
        	synchronized ( module ) {
                byte b[] = new byte[ 128 ];
                for ( int i = 0; i < 128 ; i++ )
                    b[i] = module[ OFFSET + 1 + i ];
                return b;
            }
        }
        
        public void setStrategy(byte run[]) {
        	synchronized ( module ) {
                for ( int i = 0; i < 128 ; i++ )
                	module[ OFFSET + 1 + i ] = run[i];
            }
        }
        
    }
    
    public class DHO{
    	private static final int OFFSET = 16992;
    	
    	public byte getCar_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET ];
            }
        }

        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET ] = car_message;
            }
        }

        public byte getHall_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 1 ] ;
            }
        }

        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = hall_message;
            }
        }
        
        public byte getHallStrategy () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 2 ] ;
            }
        }

        public void setHallStrategy ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }
    }
    
    /**
     * Emergengy battery Operation
     */
    public class EPB{
    	private static final int OFFSET = 17005;
    	
    	public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }

        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public boolean isFrontBuzzerEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }

        public void setFrontBuzzerEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 1, 1 );
            }
        }
        
        public boolean isRearBuzzerEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }

        public void setRearBuzzerEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 2, 2 );
            }
        }
        
        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )module[ OFFSET + 1 ];
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = led_Behavior;
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return (byte)module[ OFFSET + 2 ] ;
            }
        }

        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = car_message;
            }
        }

        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 3 ] ;
            }
        }

        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = hall_message;
            }
        }
        
        public int getDoor_close_timer () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 4 ) / 1000;
            }
        }

        public void setDoor_close_timer ( int door_close_timer ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_timer * 1000 ), 0, module, OFFSET + 4, 4 );
            }
        }
    }
    
    /**
     * NonStop Operation
     */
    public class NONSTOP{
    	private static final int OFFSET = 17015;
    	
    	public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }

        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
    	public boolean isControlModeEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 1 ], 0, 0 ) != 0;
            }
        }

        public void setControlModeEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 1 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 1 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public byte[] getControlPaneltable () {
            synchronized ( module ) {
                byte b[] = new byte[ 128 ];
                for ( int i = 0; i < 128;  i++ )
                    b[ i ] = module[ OFFSET + 2 + i ];
                return b;
            }
        }


        public void setControlPaneltable ( byte table[] ) {
            synchronized ( module ) {
                for ( int i = 0 ; i < 128 ; i++ )
                	module[ OFFSET + 2 + i ] = table[ i ];
            }
        }
        
        public byte[] getCarLockStrategytable () {
            synchronized ( module ) {
                byte b[] = new byte[ 128 ];
                for ( int i = 0; i < 128;  i++ )
                    b[ i ] = module[ OFFSET + 130 + i ];
                return b;
            }
        }


        public void setCarLockStrategytable ( byte table[] ) {
            synchronized ( module ) {
                for ( int i = 0 ; i < 128 ; i++ )
                	module[ OFFSET + 130 + i ] = table[ i ];
            }
        }
        
        public byte[] getHallLockStrategytable () {
            synchronized ( module ) {
                byte b[] = new byte[ 128 ];
                for ( int i = 0; i < 128;  i++ )
                    b[ i ] = module[ OFFSET + 258 + i ];
                return b;
            }
        }


        public void setHallLockStrategytable ( byte table[] ) {
            synchronized ( module ) {
                for ( int i = 0 ; i < 128 ; i++ )
                	module[ OFFSET + 258 + i ] = table[ i ];
            }
        }
    }
    
    /**
     * Air condition Drain operation.
     */
    public class ACDO {
        private static final int OFFSET = 17406;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }

        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }
        
        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }

        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }

        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }

        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }

        public int getReturn_floor () {
            synchronized ( module ) {
                return module[ OFFSET + 3 ];
            }
        }

        public void setReturn_floor ( int return_floor ) {
            synchronized ( module ) {
                module[ OFFSET + 3 ] = ( byte )return_floor;
            }
        }
    }
    
    /**
     * Motor overheating operation.
     */
    public class MOH {
    	private static final int OFFSET = 17415;
        
        public boolean isEnabled () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 0, 0 ) != 0;
            }
        }


        public void setEnabled ( boolean enabled ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enabled ? 1 : 0, 0, 0 );
            }
        }


        public boolean isEnable_energy_saving_on_car () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 1, 1 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_car ( boolean enable_energy_saving_on_car ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_car ? 1 : 0, 1, 1 );
            }
        }


        public boolean isEnable_energy_saving_on_hall () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 2, 2 ) != 0;
            }
        }


        public void setEnable_energy_saving_on_hall ( boolean enable_energy_saving_on_hall ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], enable_energy_saving_on_hall ? 1 : 0, 2, 2 );
            }
        }


        public boolean isDisable_cabin_fan () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 3, 3 ) != 0;
            }
        }


        public void setDisable_cabin_fan ( boolean disable_cabin_fan ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_fan ? 1 : 0, 3, 3 );
            }
        }


        public boolean isDisable_cabin_light () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 0 ], 4, 4 ) != 0;
            }
        }


        public void setDisable_cabin_light ( boolean disable_cabin_light ) {
            synchronized ( module ) {
                module[ OFFSET + 0 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 0 ], disable_cabin_light ? 1 : 0, 4, 4 );
            }
        }

        public byte getCar_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 1 ];
            }
        }


        public void setCar_message ( byte car_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 1 ] = car_message;
            }
        }


        public byte getHall_message () {
            synchronized ( module ) {
            	return module[ OFFSET + 2 ];
            }
        }


        public void setHall_message ( byte hall_message ) {
            synchronized ( module ) {
            	module[ OFFSET + 2 ] = hall_message;
            }
        }
        

        public byte getLedBehavior () {
            synchronized ( module ) {
                return ( byte )Endian.getBitsValue( module[ OFFSET + 3 ], 0, 1 );
            }
        }

        public void setLedBehavior ( byte led_Behavior ) {
            synchronized ( module ) {
            	module[ OFFSET + 3 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 3 ], led_Behavior, 0, 1 );
            }
        }


        public int getEnable_energy_saving_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 4 ) / 1000;
            }
        }


        public void setEnable_energy_saving_activation_time ( int enable_energy_saving_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( enable_energy_saving_activation_time * 1000 ), 0, module, OFFSET + 4, 4 );
            }
        }


        public int getDisable_cabin_fan_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 8 ) / 1000;
            }
        }


        public void setDisable_cabin_fan_activation_time ( int disable_cabin_fan_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_fan_activation_time * 1000 ), 0, module, OFFSET + 8, 4 );
            }
        }


        public int getDisable_cabin_light_activation_time () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 12 ) / 1000;
            }
        }


        public void setDisable_cabin_light_activation_time ( int disable_cabin_light_activation_time ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( disable_cabin_light_activation_time * 1000 ), 0, module, OFFSET + 12, 4 );
            }
        }
        
        public boolean isEnable_front_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 0, 0 ) != 0;
            }
        }
        
        public void setEnable_front_buzzer ( boolean enable_front_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_front_sgs ? 1 : 0, 0, 0 );
            }
        }
        
        public boolean isEnable_rear_buzzer () {
            synchronized ( module ) {
                return Endian.getBitsValue( module[ OFFSET + 16 ], 1, 1 ) != 0;
            }
        }
        
        public void setEnable_rear_buzzer ( boolean enable_rear_sgs ) {
            synchronized ( module ) {
                module[ OFFSET + 16 ] = ( byte )Endian.setBitsValue( module[ OFFSET + 16 ], enable_rear_sgs ? 1 : 0, 1, 1 );
            }
        }
        
        public int getDoor_close_timer () {
            synchronized ( module ) {
                return Endian.getInt( module, OFFSET + 17 ) / 1000;
            }
        }

        public void setDoor_close_timer ( int door_close_timer ) {
            synchronized ( module ) {
                System.arraycopy( Endian.getIntByteArray( door_close_timer * 1000 ), 0, module, OFFSET + 17, 4 );
            }
        }
    	
    }
}

