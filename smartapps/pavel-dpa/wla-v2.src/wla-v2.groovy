/**
 *  wattering ladscape advanced v2
 *
 *  Copyright 2021 Pavlo Dubovyk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *.
 */
 
definition(
    name: "WLA_v2",
    namespace: "pavel-dpa",
    author: "Pavlo Dubovyk",
    pausable: true,
    description: "advance control watering in my terirory",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

/* TODO
 
 - remind mm of raine to calculcate next wattring right
 - valve by mm per hour
 - valve by mm per hour + weather today yesterday - tomorrow

*/




preferences {

	section("Timers")
    {
		input "start_before_W", "time", title: "First end (normally 4:30AM)",  required: true
        input "start_after_W", "time", title: "Second start (21:50AM )", required: false
	}	       
        section("Sunrize-Sunset")
    {
        input "Sunrize_check_info", "bool", title: "Would you like use sunerise?",  required: true
        input "Sunset_check_info", "bool", title: "Would you like use suneset?",  required: true
        input "Sunrize_delay", "number", title: "Sunrize delay, min", required: false, defaultValue: 0
        input "Sunset_delay", "number", title: "Sunset delay, min", required: false, defaultValue: 0

	}
    	section("Start points")
    {
        
        input "order_start_number", "number", title: "Number for start (0 is auto)", required: true, defaultValue: 0
        input "timer_start_process", "number", title: "1-only morning 2-only evening 0-auto", required: true, defaultValue: 0 //1-morning 2-evening 0-auto

	}
       section("Temp and Raine")
       {
    
    	input "Min_temp_start", "number", title: "Min temperature for wattering, C", required: true, defaultValue: 3
      	input "Rain_check_value", "number", title: "Number Raine, mm", required: true, defaultValue: 6
        
		}
       section("Paterns config")
    {
    
    	//patern = 4 means start on 4 and 1, 3 start on 3 and 1, 2 on 2 and 1(2 times per day) :)
    
        input "Patern_schedule_1", "number", title: "Tiny approach", required: false, defaultValue: 6
        input "Max_temp_schedule_1", "number", title: "Min temp, C", required: false, defaultValue: 15
        
        input "Patern_schedule_2", "number", title: "Regular approach", required: false, defaultValue: 4
  	  	input "Max_temp_schedule_2", "number", title: "Reg temp, C", required: false, defaultValue: 25
        
        input "Patern_schedule_3", "number", title: "Mid approach", required: false, defaultValue: 3
        input "Max_temp_schedule_3", "number", title: "Max temp, C", required: false, defaultValue: 30
                
        input "Patern_schedule_4", "number", title: "High approach", required: false, defaultValue: 2


    }
  
      section("Valves to adjust...")
    {
		input "valve_main", "capability.switch",title: "Main valve", required: true, multiple: false

        input "valve01", "capability.switch", title: "Zone 1 valve", required: true, multiple: false
		input "valve01_time", type: "number" , title: "Zone 1 - time", required: false, defaultValue: 7
        input "valve01_session_count", type: "number" , title: "Zone 1 - count",required: false, defaultValue: 2
        
        input "valve02", "capability.switch", title: "Zone 2 valve", required: false, multiple: false
		input "valve02_time", type: "number" , title: "Zone 2 - time", required: false, defaultValue: 7
        input "valve02_session_count", type: "number" , title: "Zone 2 - count",required: false, defaultValue: 2       
        
        input "valve03", "capability.switch", title: "Zone 3 valve", required: false, multiple: false
		input "valve03_time", type: "number" , title: "Zone 3 - time", required: false, defaultValue: 5
        input "valve03_session_count", type: "number" , title: "Zone 3 - count",required: false, defaultValue: 1       
        
        input "valve04", "capability.switch", title: "Zone 4 valve", required: false, multiple: false
		input "valve04_time", type: "number" , title: "Zone 4 - time", required: false, defaultValue: 10
        input "valve04_session_count", type: "number" , title: "Zone 4 - count",required: false, defaultValue: 1       
        
        input "valve05", "capability.switch", title: "Zone 5 valve", required: false, multiple: false
		input "valve05_time", type: "number" , title: "Zone 5 - time", required: false, defaultValue: 7
        input "valve05_session_count", type: "number" , title: "Zone 5 - count",required: false, defaultValue: 2  
        
        input "valve06", "capability.switch", title: "Zone 6 valve", required: false, multiple: false
		input "valve06_time", type: "number" , title: "Zone 6 - time", required: false, defaultValue: 5
        input "valve06_session_count", type: "number" , title: "Zone 6 - count",required: false, defaultValue: 1
        
        input "valve07", "capability.switch", title: "Zone 7 valve", required: false, multiple: false
		input "valve07_time", type: "number" , title: "Zone 7 - time", required: false, defaultValue: 10
        input "valve07_session_count", type: "number" , title: "Zone 7 - count",required: false, defaultValue:2
        
        input "valve08", "capability.switch", title: "Zone 8 valve", required: false, multiple: false
		input "valve08_time", type: "number" , title: "Zone 8 - time", required: false, defaultValue:10
        input "valve08_session_count", type: "number" , title: "Zone 8 - count",required: false, defaultValue:2
	}
    
	section("Send Notifications?") {
               input "people", "capability.presenceSensor", multiple: true

    }
}

def installed() {
	//log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	//log.debug "Updated with settings: ${settings}"

	unsubscribe()
    unschedule()
	//initialize()
    wattering_init_setup() 
    
}

def initialize() {
	
        
    state.order_patern = 0
    state.order_patern_num = 0
	wattering_init_setup()   

   
}


def set_schedulers(message_type)
{


//1 - find out about timers (time or offset)

def off_calc = calculate_the_offset()
log.debug "Offset : ${off_calc}" 
	

//2 - set schedulers
unschedule()

        
        def time_delay_valve_min = off_calc / 60

		def Sunrize_delay_FULL = -1*(Sunrize_delay+time_delay_valve_min)
        log.debug "Sunrize_delay_FULL: ${Sunrize_delay_FULL}"
    	def Sunset_Sunrise = getSunriseAndSunset(sunriseOffset: Sunrize_delay_FULL, sunsetOffset: Sunset_delay)
		log.debug "GET SUN SET: ${Sunset_Sunrise}"
				
        def sunset_offset = Sunset_Sunrise.sunset
        def sunrise_offset = Sunset_Sunrise.sunrise
        
      
		def midnightTime = (new Date().clearTime() + 1)
		log.debug "midnightTime : ${midnightTime}"
        
        def timer_1_date = new Date()
        def timer_2_date = new Date()
        
        def schedule_message_str1 = ''
        def schedule_message_str2 = ''
        
        //define dates for comapre
      

        
        
//DATES CALCULATIONS
if (Sunrize_check_info)
	{
    	//using sunrize offsets
        // each wattering run
		
        
          
        
        def between_second = timeOfDayIsBetween(sunrise_offset, midnightTime, new Date(), location.timeZone)
        if (between_second)
        {
            //shedule for next day
			def Sunset_Sunrise_tomorrow = getSunriseAndSunset(sunriseOffset: Sunrize_delay_FULL, sunsetOffset: Sunset_delay, date: new Date()+1)
  			def sunrise_offset_tomorrow =Sunset_Sunrise_tomorrow.sunrise.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
			
            ///Refactoring
            /// compare    -   sunrise_offset_tomorrow
            ///schedule(sunrise_offset_tomorrow,wattering)
            ///sendMessage("Watterind setuped for next day at: $sunrise_offset_tomorrow",message_type)
           
             timer_1_date = sunrise_offset_tomorrow
             schedule_message_str1 = 'Watterind setuped for next day at: '//+sunrise_offset_tomorrow//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
             
            log.debug "Schedules sunset wattering for next day at: $sunrise_offset_tomorrow"
                       
            
            
        }
        else
        {   
            //shedule for today
            
           /// compare    -   Sunset_Sunrise.sunrise
           ///schedule(Sunset_Sunrise.sunrise.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone),wattering)
		  ///sendMessage("Watterind setuped for today at : $sunrise_offset",message_type)
        
          timer_1_date = Sunset_Sunrise.sunrise.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
		  schedule_message_str1 = 'Watterind setuped for today at : '//+sunrise_offset.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)

          log.debug "Schedules sunrise wattering for today: $sunrise_offset" 
           
        }
        
    }
    else
    {
    	//using regular timer offsets
        //FIRST RUN by timers
 	  def processing_time = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", start_before_W,location.timeZone)
	  def start_b_w_time = new Date( processing_time.time - off_calc * 1000).format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
      
      def processing_time_Final = Date.parse("yyyy-MM-dd'T'HH:mm:ss", start_b_w_time,location.timeZone)      
      
      //OLD//def sch_string =  "00"+processing_time_Final.hours + " " + processing_time_Final.minutes+ " * * * ?"	
       def sch_string =  "0 "+processing_time_Final.minutes+" "+processing_time_Final.hours + " ? * MON-SUN"	
		
		
        /// compare    -   start_b_w_time
        ///schedule(sch_string,wattering)
        ///sendMessage("Watterind setuped to : $processing_time_Final",message_type)       
	
		timer_1_date = start_b_w_time
		schedule_message_str1 = 'Watterind setuped to : '//+processing_time_Final.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
        
        
        log.debug "Schedules wattering for: $timer_1_date" 

        
    }






if (Sunset_check_info)
	{
    	//using sunrize offsets
       
        
		//def between = timeOfDayIsBetween(sunrise_offset, sunset_offset, new Date(), location.timeZone)
        def between = timeOfDayIsBetween(timer_1_date, sunset_offset, new Date(), location.timeZone)
		if (between)
        {
         //Schedule for today
			
            /// compare    -   Sunset_Sunrise.sunset
            ///schedule(Sunset_Sunrise.sunset.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone),wattering)
			///sendMessage("Watterind setuped for today at : $sunset_offset",message_type)          

			timer_2_date = Sunset_Sunrise.sunset//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
            schedule_message_str2 = 'Watterind setuped for today at : '//+timer_2_date//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
            
            
            log.debug "Schedules sunset wattering for today at: $sunset_offset"
            
        } else        
        {
         //Schedule for tomorrow
         	
            /// compare    -   Sunset_Sunrise_tomorrow.sunset
            def tomm_dat = (new Date()+1)
			def Sunset_Sunrise_tomorrow = getSunriseAndSunset(sunriseOffset: Sunrize_delay_FULL, sunsetOffset: Sunset_delay, date: tomm_dat)
			///schedule(Sunset_Sunrise_tomorrow.sunset.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone),wattering)
			///sendMessage("Watterind setuped for tomorrow at : $sunset_offset",message_type)         
          
            timer_2_date = Sunset_Sunrise_tomorrow.sunset//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
            schedule_message_str2 = 'Watterind setuped for tomorrow at : '//+timer_2_date//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
            
            log.debug "Schedules sunset wattering for tomorrow at: $timer_2_date"
        }
        
    }
    else
    { if (start_after_W) {       
		//Second RUN by timers
 		def processing_time_Final_A = Date.parse("yyyy-MM-dd'T'HH:mm:ss", start_after_W,location.timeZone)
        def start_aftre_w_time = new Date(processing_time_Final_A.time).format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
        
        //OLD//def sch_string_A =  processing_time_Final_A.hours + " " + processing_time_Final_A.minutes+ " * * * ?"	
        //LAST//def sch_string_A =  "0 "+processing_time_Final_A.minutes+" "+processing_time_Final_A.hours + " ? * MON-SUN"	
	    
        
		/// compare    -   start_aftre_w_time
        ///schedule(sch_string_A,wattering)
        ///sendMessage("Watterind setuped to : $processing_time_Final_A",message_type)
		
        schedule_message_str2 = 'Watterind setuped to : '//+processing_time_Final_A.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
		timer_2_date = start_aftre_w_time


        log.debug "schedules wattering for: $processing_time_Final_A"                  
        }
    }

//END CALCULATIONS

//START SHEDULING

def set_clear_timer_date1 = timeTodayAfter(new Date(),timer_1_date,location.timeZone)



if (state.timer_start_first==0)
            {
             //AUTO option to start
                        if (start_after_W || Sunset_check_info)
                        {
                        //def set_clear_timer_date2 = timeTodayAfter(new Date(),timer_2_date,location.timeZone)

                        def set_clear_timer_date2 = timer_2_date

                        if (Sunset_check_info == false)
                            { set_clear_timer_date2 = timeTodayAfter(new Date(),timer_2_date,location.timeZone)}

                        log.debug "set_clear_timer_date1 : $set_clear_timer_date1"                  
                        log.debug "set_clear_timer_date2 : $set_clear_timer_date2"                  



                        // TO DO - reuse evening_wattering & morning_wattering
                            if (set_clear_timer_date1>set_clear_timer_date2)
                                {
                                    log.debug "set_clear_timer_date2 CHECK: $set_clear_timer_date2"                  
                                    schedule(set_clear_timer_date2,evening_wattering)

                                    schedule_message_str2 = schedule_message_str2 + set_clear_timer_date2.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
                                    sendMessage(schedule_message_str2,message_type)
                                }
                                else
                                {
                                    log.debug "set_clear_timer_date1 CHECK : $set_clear_timer_date1"                  
                                    schedule(set_clear_timer_date1,morning_wattering)

                                    schedule_message_str1 = schedule_message_str1 + set_clear_timer_date1.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
                                    sendMessage(schedule_message_str1,message_type)
                                }

                        }

                        else

                        {
                        // ONLY FIRST RUN
                                    log.debug "set_clear_timer_date1 CHECK ONLY : $set_clear_timer_date1"  
                                    schedule_message_str1 = schedule_message_str1 + set_clear_timer_date1.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)


                                    schedule(set_clear_timer_date1,morning_wattering)
                                    sendMessage(schedule_message_str1,message_type)
                        }

            }
            else
            {
            
            	if (timer_1_date && state.timer_start_first==1)
                        {
                         //only morning starts

                                    log.debug "morning start CHECK ONLY : $timer_1_date"  
                                    if (Sunrize_check_info) 
                                       {
											schedule_message_str1 = schedule_message_str1+ timer_1_date//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)

                                       }
                                       else
                                       {
											schedule_message_str1 = schedule_message_str1+ timer_1_date//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
                                       }
                                    
                                    
                                    schedule(timer_1_date,morning_wattering)
                                    sendMessage(schedule_message_str1,message_type)
                        }
            
				if (timer_2_date && state.timer_start_first==2)
                            {
                             //only evening starts
                                     log.debug "evening start CHECK ONLY : $timer_1_date"  
                                     if (Sunset_check_info) 
                                       {
                                          schedule_message_str2 = schedule_message_str2 + timer_2_date.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
										}
                                        else
                                        {
                                          schedule_message_str2 = schedule_message_str2 + timer_2_date//.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",location.timeZone)
                                        }

                                        schedule(timer_2_date,evening_wattering)
                                        sendMessage(schedule_message_str2,message_type)
                            }	           
            
            }
 
 
	state.timer_start_first = 0



//END SCEDULING



}


def morning_wattering()
{
	//for future schedule
    state.timer_start_first = 2
    
	wattering ()
}

def evening_wattering()
{
	//for future schedule
    state.timer_start_first = 1
    
	wattering()
}


def calculate_the_offset()
{

def offset_calc=0
def max_session_counts_offset = 0

try
    	{
		if (valve01)
        	{ if (valve01_time.toInteger()>0 && valve01_session_count.toInteger()>0)  
        		{
                	offset_calc=offset_calc+valve01_time.toInteger()*valve01_session_count.toInteger()*60
             		if (valve01_session_count.toInteger()>max_session_counts_offset) 
                    		{max_session_counts_offset = valve01_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #1 : ${offset_calc}"         
            
        if (valve02 )
        	{ if (valve02_time.toInteger()>0 && valve02_session_count.toInteger()>0)  
            	{
                	offset_calc=offset_calc+valve02_time.toInteger()*valve02_session_count.toInteger()*60
            		if (valve02_session_count.toInteger()>max_session_counts_offset) 
                    	{max_session_counts_offset = valve02_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #2 : ${offset_calc}"
            
        if (valve03)
        	{ if (valve03_time.toInteger()>0 && valve03_session_count.toInteger()>0)  
            	{
                	offset_calc=offset_calc+valve03_time.toInteger()*valve03_session_count.toInteger()*60
            		if (valve03_session_count.toInteger()>max_session_counts_offset) 
                    	{max_session_counts_offset = valve03_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #3 : ${offset_calc}"
            
        if (valve04)
        	{ if ( valve04_time.toInteger()>0 && valve04_session_count.toInteger()>0)  
            	{
                	offset_calc=offset_calc+valve04_time.toInteger()*valve04_session_count.toInteger()*60
            		if (valve04_session_count.toInteger()>max_session_counts_offset) 
                    	{max_session_counts_offset = valve04_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #4 : ${offset_calc}"
            
 		if (valve05 )
        	{ if ( valve05_time.toInteger()>0 && valve05_session_count.toInteger()>0)  
        	{
            		offset_calc=offset_calc+valve05_time.toInteger()*valve05_session_count.toInteger()*60
            		if (valve05_session_count.toInteger()>max_session_counts_offset) 
                    		{max_session_counts_offset = valve05_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #5 : ${offset_calc}"
            
        if (valve06 )
        	{ if ( valve06_time.toInteger()>0 && valve06_session_count.toInteger()>0)  
            {
            		offset_calc=offset_calc+valve06_time.toInteger()*valve06_session_count.toInteger()*60
            		if (valve06_session_count.toInteger()>max_session_counts_offset) 
                			{max_session_counts_offset = valve06_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #6 : ${offset_calc}"
            
        if (valve07 )
        	{ if ( valve07_time.toInteger()>0 && valve07_session_count.toInteger()>0)  
            	{	
                	offset_calc=offset_calc+valve07_time.toInteger()*valve07_session_count.toInteger()*60
            		if (valve07_session_count.toInteger()>max_session_counts_offset) 
                    		{max_session_counts_offset = valve07_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #7 : ${offset_calc}"
            
        if (valve08 )
        	{ if ( valve08_time.toInteger()>0 && valve08_session_count.toInteger()>0)  
            	{
                	offset_calc=offset_calc+valve08_time.toInteger()*valve08_session_count.toInteger()*60
            		if (valve08_session_count.toInteger()>max_session_counts_offset) 
                    		{max_session_counts_offset = valve08_session_count.toInteger()}
            	}
            }
            //log.debug "offset_calc #8 : ${offset_calc}"
 
  }
   		catch (e)
		{
        sendMessage("Check: Exception $e", false)
		}
   
    //3 min for each round for internal communication
    offset_calc = offset_calc + max_session_counts_offset*180
   
   	return offset_calc
 
}



def wattering_init_setup()
{

	 state.wheater_data_temp = accuweather_forecast_12()

    
    state.rain_current = state.wheater_data_temp[0]
    state.rain_history = accuweather_Historical_Current()
    state.timer_start_first = timer_start_process
    
    
    set_schedulers(true)
	/*if (state.order_patern_num ==0)
    	{set_schedulers(true)}
        else
        {set_schedulers(false)}
    */    
        state.VALVE_SESSION = 1
		state.VALVE_NUMBER = 1

      Patern_check_scheduler()             

	if (order_start_number!=0)
    	{
			log.debug "initial Patern# before update: ${state.order_patern}"
			state.order_patern = order_start_number
			sendMessage ("Initial order update $state.order_patern", true)

		}
	log.debug "initial Patern# : ${state.order_patern}" 
    
	log.debug "Initial order_patern_num : ${state.order_patern_num}" 
    
     
    
    
    
}

def wattering()
{

   
    state.wheater_data_temp = accuweather_forecast_12()

    def forecast_12 = state.wheater_data_temp[0]
	def rain_data_12 = accuweather_Historical_Current()
    
    state.rain_current = state.wheater_data_temp[0]
    state.rain_history = rain_data_12
    
    
    def rain_history_forecast = state.rain_current  + state.rain_history
    
    def day_min_temp = state.wheater_data_temp[3]
    def day_min_temp_rf =  state.wheater_data_temp[4]
    
    


    def day_min_temp_comp = day_min_temp
    
    if (day_min_temp_comp>day_min_temp_rf) {day_min_temp_comp=day_min_temp_rf}
    
    
	def Patern_schedule = -1
    if (state.order_patern_num ==1) {Patern_schedule=Patern_schedule_1}
    if (state.order_patern_num ==2) {Patern_schedule=Patern_schedule_2}
    if (state.order_patern_num ==3) {Patern_schedule=Patern_schedule_3}
    if (state.order_patern_num ==4) {Patern_schedule=Patern_schedule_4}

    	log.debug "Wattering patern $Patern_schedule" 

//wattering

if (day_min_temp_comp>Min_temp_start)
{
	log.debug "Wattering min_temp pass  $day_min_temp_comp" 
    log.debug "rain data $rain_history_forecast" 
     
     if (rain_history_forecast<Rain_check_value)
     
            {
                log.debug "Rain threshold pass" 
                

        if (state.order_patern == Patern_schedule || state.order_patern == 1)          
               {
               		log.debug "Wattering pattern pass" 
                    sendMessage ("Watterind started", true)

                    state.VALVE_SESSION = 1
                    state.VALVE_NUMBER = 1

                    valve_main.on()
                    wattering_start()
                }
                else
                {
                	//if we skip run based on pattern
                	log.debug "Pattern skip run" 

                	wattering_exit(3)
                }
            }
            else
            {
				log.debug "EXIT due to the raine $rain_history_forecast"

                 wattering_exit(1)
                 //EXIT DUE TO RAIN
                 sendMessage ("Watterind aborted due to the raine threshold is $rain_history_forecast", true)
            }

    }
    
    else
    {
    	//Pass wattering due to low temp
             sendMessage ("Watterind aborted due to the low temp threshold is $day_min_temp_comp", true)
			wattering_exit(2)
            //do now changes anything just reschedule
    
    }
    
        
        
	//EXIT
}

def wattering_start()
{
    //run wattering 

    switch (state.VALVE_NUMBER)
    {
        case {it==1}:
            if (valve01 && valve01_time && valve01_session_count) 
        {
		 if (valve01_session_count.toInteger()*valve01_time.toInteger()>0 && valve01_session_count.toInteger()>=state.VALVE_SESSION)
            { 
            log.debug "watterting 1"  

            def v1_time = valve01_time.toInteger()
            log.debug "wattering v1 start for, min $v1_time"
            valve01.on()
            runIn(v1_time*60,valves_off)   			
        	} 
        	else 
        	{
            	log.debug "valve v1 exiting"
            	runIn(2,valves_off)
        	}
         } else {
            		log.debug "valve v1 exiting"
            		runIn(2,valves_off)
        			}
        break;

        case {it==2}:
            if (valve02 && valve02_time && valve02_session_count) 
        {
			if (valve02_session_count.toInteger()*valve02_time.toInteger()>0 && valve02_session_count.toInteger()>=state.VALVE_SESSION)
            { 
            log.debug "watterting 2"  

            def v2_time = valve02_time.toInteger()
            log.debug "wattering v2 start for, min $v2_time"
            valve02.on()
            runIn(v2_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v2 exiting"
                runIn(2,valves_off)
            }
        }else {
            		log.debug "valve v2 exiting"
            		runIn(2,valves_off)
        			}
        break;
        
        case {it==3}:

            if (valve03 && valve03_time && valve03_session_count) 
        {
		 if (valve03_session_count.toInteger()*valve03_time.toInteger()>0 && valve03_session_count.toInteger()>=state.VALVE_SESSION)
            { 
            	log.debug "watterting 3"  

                def v3_time = valve03_time.toInteger()
                log.debug "wattering v3 start for, min $v3_time"
                valve03.on()
                runIn(v3_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v3 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v3 exiting"
            		runIn(2,valves_off)
        			}
        break;
        
		case {it==4}:
            if (valve04 && valve04_time && valve04_session_count) 
        {
		if (valve04_session_count.toInteger()*valve04_time.toInteger()>0 && valve04_session_count.toInteger()>=state.VALVE_SESSION)
            { 
                log.debug "watterting 4"  

                def v4_time = valve04_time.toInteger()
                log.debug "wattering v4 start for, min $v4_time"
                valve04.on()
                runIn(v4_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v4 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v4 exiting"
            		runIn(2,valves_off)
        			}
        break;
        
        case {it==5}:
            if (valve05 && valve05_time && valve05_session_count) 
        {
		if (valve05_session_count.toInteger()*valve05_time.toInteger()>0 && valve05_session_count.toInteger()>=state.VALVE_SESSION)
            { 
                log.debug "watterting 5"  

                def v5_time = valve05_time.toInteger()
                log.debug "wattering v5 start for, min $v5_time"
                valve05.on()
                runIn(v5_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v5 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v5 exiting"
            		runIn(2,valves_off)
        			}
        break;

        case {it==6}:
            if (valve06 && valve06_time && valve06_session_count) 
        {
		if (valve06_session_count.toInteger()*valve06_time.toInteger()>0 && valve06_session_count.toInteger()>=state.VALVE_SESSION)
            { 
                log.debug "watterting 6"  

                def v6_time = valve06_time.toInteger()
                log.debug "wattering v6 start for, min $v6_time"
                valve06.on()
                runIn(v6_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v6 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v6 exiting"
            		runIn(2,valves_off)
        			}
        break;
        
        case {it==7}:
            if (valve07 && valve07_time && valve07_session_count) 
        {
		if (valve07_session_count.toInteger()*valve07_time.toInteger()>0 && valve07_session_count.toInteger()>=state.VALVE_SESSION)
            { 
                log.debug "watterting 7"  

                def v7_time = valve07_time.toInteger()
                log.debug "wattering v7 start for, min $v7_time"
                valve07.on()
                runIn(v7_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v7 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v7 exiting"
            		runIn(2,valves_off)
        			}
        break;
        
		case {it==8}:
            if (valve08 && valve08_time && valve08_session_count) 
        {
		if (valve08_session_count.toInteger()*valve08_time.toInteger()>0 && valve08_session_count.toInteger()>=state.VALVE_SESSION)
            { 
                log.debug "watterting 8"  

                def v8_time = valve08_time.toInteger()
                log.debug "wattering v8 start for, min $v8_time"
                valve08.on()
                runIn(v8_time*60,valves_off)   			
            } 
            else 
            {
                log.debug "valve v8 exiting"
                runIn(2,valves_off)
            }
        } else {
            		log.debug "valve v8 exiting"
            		runIn(2,valves_off)
        			}
        break;
    }
}


def valves_off()
{

   def max_session = 0

if (valve01 && valve01_session_count) {
	if (valve01_session_count.toInteger()>max_session)	
            	{max_session=valve01_session_count.toInteger()}
			}
if (valve02 && valve02_session_count) {
	if (valve02_session_count.toInteger()>max_session)	
            	{max_session=valve02_session_count.toInteger()}
			}
if (valve03 && valve03_session_count) {
	if (valve03_session_count.toInteger()>max_session)	
            	{max_session=valve03_session_count.toInteger()}
			}
if (valve04 && valve04_session_count) {
	if (valve04_session_count.toInteger()>max_session)	
            	{max_session=valve04_session_count.toInteger()}
			}
if (valve05 && valve05_session_count) {
	if (valve05_session_count.toInteger()>max_session)	
            	{max_session=valve05_session_count.toInteger()}
			}
if (valve06 && valve06_session_count) {
	if (valve06_session_count.toInteger()>max_session)	
            	{max_session=valve06_session_count.toInteger()}
			}
if (valve07 && valve07_session_count) {
	if (valve07_session_count.toInteger()>max_session)	
            	{max_session=valve07_session_count.toInteger()}
			}
if (valve08 && valve08_session_count) {
	if (valve08_session_count.toInteger()>max_session)	
            	{max_session=valve08_session_count.toInteger()}
			}            
            
            
	log.debug "Valves off - START"
	
	if (valve01) {valve01.off()}
    if (valve02) {valve02.off()}
    if (valve03) {valve03.off()}
    if (valve04) {valve04.off()}
    if (valve05) {valve05.off()}
    if (valve06) {valve06.off()}
    if (valve07) {valve07.off()}
    if (valve08) {valve08.off()}
    
	log.debug "Valves off - DONE"


 state.VALVE_NUMBER = state.VALVE_NUMBER + 1


///////////////CONST  THIS APP FOR 8 VALVES
    if (state.VALVE_NUMBER.toInteger()<9)
        {

          log.debug "GO TO NEXT VALVE ${state.VALVE_NUMBER}"
          //wattering_start()
          runIn(2,wattering_start)
        }
        else
        { 
            //GO TO NEXT SESSION

            log.debug "Prepare next session"
            state.VALVE_NUMBER = 1
            state.VALVE_SESSION = state.VALVE_SESSION+1  

            if (state.VALVE_SESSION.toInteger()>max_session)
                {

                    log.debug "EXIT"
                    
                    runIn(2,wattering_exit_zero)
					//wattering_exit(0)
					//NORMAL EXIT AFTER WATTERING

                }
                else
                { 

                log.debug "Next session starting ${state.VALVE_SESSION} from ${max_session}"

                //continue wattering - next session
                runIn(2,wattering_start)
                //wattering_start()

                }
        }

    
}


def wattering_exit_zero()
{
	wattering_exit(0)
}


def valves_all_off()
{


	log.debug "All valves off - START"
	
    valve_main.off()	
	if (valve01) {valve01.off()}
    if (valve02) {valve02.off()}
    if (valve03) {valve03.off()}
    if (valve04) {valve04.off()}
    if (valve05) {valve05.off()}
    if (valve06) {valve06.off()}
    if (valve07) {valve07.off()}
    if (valve08) {valve08.off()}
    
	log.debug "All valves off - DONE"
    
}



def Patern_check_scheduler()
{

	def Patern_schedule = 0
    if (state.order_patern_num ==1) {Patern_schedule=Patern_schedule_1}
    if (state.order_patern_num ==2) {Patern_schedule=Patern_schedule_2}
    if (state.order_patern_num ==3) {Patern_schedule=Patern_schedule_3}
    if (state.order_patern_num ==4) {Patern_schedule=Patern_schedule_4}


    def past_12=0
   

    def forecast_12 = state.wheater_data_temp[0]
    def day_max_temp= state.wheater_data_temp[1]
    def day_max_temp_rf= state.wheater_data_temp[2]

    def day_max_temp_real = 0
 
    
	if (day_max_temp>day_max_temp_rf) 
    {
        day_max_temp_real=day_max_temp
    } else
    {
        day_max_temp_real=day_max_temp_rf
    }


	log.debug "Exit temp check -  : ${day_max_temp_real}" 
    sendMessage ("Exit temp check -  : ${day_max_temp_real}" , true)   
    log.debug "Exit max temp# : ${day_max_temp_real}" 

	log.debug "Order patern before check -  : ${state.order_patern}" 
    sendMessage ("Order patern before check -  : ${state.order_patern}" , true)  


// ALIGN WITH REGULAR FLOW AS IT RESET EVERTHING
//temp check & pattern

 switch (day_max_temp_real)
    {
    
    	  case {it>Max_temp_schedule_3 }://&& state.order_patern_num!=4}:
         	def result_patern =  Patern_schedule_4+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}        	

			log.debug "result_patern #4 -  : ${result_patern}" 
        	state.order_patern_num=4
         break;
         
         case {it>Max_temp_schedule_2 }://&& state.order_patern_num!=3}:
         	def result_patern =  Patern_schedule_3+state.order_patern - Patern_schedule  
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
        	
            log.debug "result_patern #3 -  : ${result_patern}" 
        	state.order_patern_num=3
        break;
        
        case {it>Max_temp_schedule_1 }://&& state.order_patern_num!=2}:
          	def result_patern =  Patern_schedule_2+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
           	
            log.debug "result_patern #2 -  : ${result_patern}" 
            state.order_patern_num=2 
        break;
  
         case {Max_temp_schedule_1>=it}:// && state.order_patern_num!=1}:
         	def result_patern =  Patern_schedule_1+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
            
            log.debug "result_patern #2 -  : ${result_patern}" 
        	state.order_patern_num=1
        break;
	}

/*
	if (day_max_temp_real>Max_temp_schedule_1 && state.order_patern_num!=2) 
    	{
    		def result_patern =  Patern_schedule_2+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
           	
            state.order_patern_num=2
        }
    if (day_max_temp_real>Max_temp_schedule_2 && state.order_patern_num!=3) 
    	{
        	def result_patern =  Patern_schedule_3+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
        
        state.order_patern_num=3
        }
   if (day_max_temp_real>Max_temp_schedule_3 && state.order_patern_num!=4) 
    	{
        	def result_patern =  Patern_schedule_4+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}        	
  
        state.order_patern_num=4
        }
    if (Max_temp_schedule_1>day_max_temp_real && state.order_patern_num!=1) 
    	{
        	def result_patern =  Patern_schedule_1+state.order_patern - Patern_schedule       
            if (0>=result_patern) 
            	{state.order_patern=1}
                else
                {state.order_patern=result_patern}
                
        state.order_patern_num=1
        }
*/
	log.debug "Exit Patern# : ${state.order_patern}" 
    sendMessage ("Order patern after check -  : ${state.order_patern}" , true)   
	log.debug "Exit order_patern_num : ${state.order_patern_num}" 
    
    

}



def wattering_exit(exit_type)
{


/*
exit_type
 0 - normal - patern shift
 1 - rain   - patern doesn't shift
 2 - low temp  - patern doesn't shift
 3 - patern pass  - patern shift

*/

	valves_all_off()

    
	def Patern_schedule = -1
    if (state.order_patern_num ==1) {Patern_schedule=Patern_schedule_1}
    if (state.order_patern_num ==2) {Patern_schedule=Patern_schedule_2}
    if (state.order_patern_num ==3) {Patern_schedule=Patern_schedule_3}
    if (state.order_patern_num ==4) {Patern_schedule=Patern_schedule_4}


 if (exit_type==0 || exit_type==3)
 		{
			
            state.order_patern=state.order_patern-1
			if (0>=state.order_patern){state.order_patern=Patern_schedule-1}
 		}
 
    
	state.VALVE_SESSION = 1
	state.VALVE_NUMBER = 1
    
  	sendMessage ("Watterind has done", true)   
  
  	Patern_check_scheduler()
  
	set_schedulers(true)
  	


}


def sendMessage(message , message_type)
{
	def stamp = new Date().format('hh:mm:ss ', location.timeZone)
    
  	if (people && message_type)
            {
				sendPush(stamp + "$app.label " + message)
             }
             
    /*
    if (location.contactBookEnabled && recipients) 
   	{
    	sendNotificationToContacts(stamp + "$app.label " + message, recipients)
   	}/*
   	else
  	{
   		sendSms(phone, stamp + "$app.label " + message)
  	}*/
    
    log.debug "MESSAGE: $stamp $app.label $message"
}

def accuweather_forecast_12()
{

	///http://dataservice.accuweather.com/forecasts/v1/daily/5day/1218844?apikey=MNRps0GCqAbArykpkM5zj6bPShIRKT2y&details=true&metric=true"
	def keylocation = "1219063" // location key based on accuweather data - you can get that on their website
    def APIkey = "MNRps0GCqAbArykpkM5zj6bPShIRKT2y"
    def details = "true"
	def metrics = "true"
    
    def  response = ""
  	def  response_result = []
  
  //  def rain_next_6_hrs = 0
    
	
    def URI_HTTP ="http://dataservice.accuweather.com/forecasts/v1/daily/1day/$keylocation?apikey=$APIkey&details=$details&metric=$metrics" 
	//def URI_HTTP ="http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/$keylocation?apikey=$APIkey&details=$details&metric=$metrics" 
	//log.debug "URI_HTTP: $URI_HTTP"
    
	try
    	{
                
                
                	httpGet(uri: URI_HTTP,contentType: 'application/json',)
                				{resp ->           
                           // log.debug "resp data: ${resp.data}"
                          //  log.debug "result: ${resp.data.result}" 
									response = resp.data

								}
							
						//	log.debug "result: $response"
                            
							def day_temp = response?.DailyForecasts?.Temperature?.Maximum?.Value
                            def night_temp = response?.DailyForecasts?.Temperature?.Minimum?.Value
                            
                            
                            def day_temp_RF = response?.DailyForecasts?.RealFeelTemperature?.Maximum?.Value
                            def night_temp_RF = response?.DailyForecasts?.RealFeelTemperature?.Minimum?.Value
                            
                          //  log.debug "DAY_RF: $day_temp_RF"
                            
                            def total_l = response?.DailyForecasts?.Day?.TotalLiquid?.Value+response?.DailyForecasts?.Night?.TotalLiquid?.Value
                            
                            
                            
                            
                          //  log.debug "total liquid amount mm: $total_l"
   							
                            //12 hours approach
                            //def rain_next_6_hrs =  total_l[0]+total_l[1]+total_l[2]+total_l[3]+total_l[4]+total_l[5]
                            //rain_next_6_hrs = rain_next_6_hrs + total_l[6]+total_l[7]+total_l[8]+total_l[9]+total_l[10]+total_l[11]
                            
                            //1 day approach
                            def rain_next_6_hrs =  total_l[0]
                            
                            
  							response_result << rain_next_6_hrs
                            response_result << day_temp[0]
							response_result << day_temp_RF[0]
                            response_result << night_temp[0]
                            response_result << night_temp_RF[0]

                            
                          //  log.debug "rain_result: $rain_next_6_hrs" 
                                                       

						                                          		
         }
   		catch (e)
		{
        sendMessage("Check: Exception $e", false)
		}
   
   	return response_result

   
   /*
   
    if (rain_next_6_hrs!=null)
		{
        	log.debug "return rain_result: $rain_next_6_hrs"     
			return rain_next_6_hrs
		}  else
        {
			log.debug "fasle return rain_result ==== 0"     
				return 0 
		}
   */  
}



def accuweather_Historical_Current()
{

	///http://dataservice.accuweather.com/forecasts/v1/daily/5day/1218844?apikey=MNRps0GCqAbArykpkM5zj6bPShIRKT2y&details=true&metric=true"
	def keylocation = "1219063" // location key based on accuweather data - you can get that on their website
    def APIkey = "MNRps0GCqAbArykpkM5zj6bPShIRKT2y"
    def details = "true"
	def metrics = "true"
	
    def  response = ""
	def rain_12_hrs = 0
    

	def URI_HTTP ="http://dataservice.accuweather.com/currentconditions/v1/$keylocation/historical/24?apikey=$APIkey&details=$details" 
	//log.debug "URI_HTTP: $URI_HTTP"
    
	try
    	{
                
                
                	httpGet(uri: URI_HTTP,contentType: 'application/json',)
                				{resp ->           
                           // log.debug "resp data: ${resp.data}"
                          //  log.debug "result: ${resp.data.result}" 
									response = resp.data

								}
							
							//log.debug "result: $response"
                            
        
                            //def total_l = response?.PrecipitationSummary?.Past12Hours?.Metric?.Value
                            def total_l = response?.PrecipitationSummary?.Past24Hours?.Metric?.Value
                            
                           
                            //log.debug "TOTLA L : $total_l"
                            
                            rain_12_hrs =  total_l[0]
                            
                            
							log.debug "rain_history_hrs: $rain_12_hrs"
                            
							                            
                            //log.debug "rain_result: $rain_result" 
                                                       

						                                          		
         }
   		catch (e)
		{
        sendMessage("Check: Exception $e", false)
		}
   
   
   			return rain_12_hrs

   
   /*
     if (rain_12_hrs!=null)
		{
        	log.debug "return last 12 rain_result: $rain_12_hrs"     
			return rain_12_hrs
		}  else
        {
			log.debug "fasle last 12 return rain_result ==     0"     
				return 0
		}
    */ 
}