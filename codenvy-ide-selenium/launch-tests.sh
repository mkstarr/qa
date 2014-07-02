tenantName=exoinvitemain
tenantName2=additional.test.user
tenantName3=selenium3
tenantName4=selenium4
######################
tenantName5=codenvymain
tenantName6=additional2test.user
tenantName7=selenium3_2
tenantName8=selenium4_2


commands="$1 $2 $3 $4 $5"
if [[ "$commands" == *-stg.com* ]]; then
	host=https://$1
else
	host=http://$1
fi



if [[ "$commands" == *FIREFOX* ]]; then

    echo "Recreating tenants for FIREFOX"
    echo "=================================="
    echo Stoping tenant: $tenantName5
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName5
    echo Deleting tenant: $tenantName5
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName5
    echo Creating new tenant: $tenantName5
    token=`curl -X POST -H "Content-Type: application/json" -d '{"username":"codenvymain@gmail.com","password":"eXoUATest14"}' $host/api/auth/login | sed 's/token//g' | sed 's/[{}:"]//g'`
    curl -X POST -H "Content-Type: application/json" -d '{"name":"codenvymain"}' $host/api/workspace/create?token=$token

    echo Stoping tenant: $tenantName6
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName6
    echo Deleting tenant: $tenantName6
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName6
    echo Creating new tenant: $tenantName6
    token=`curl -X POST -H "Content-Type: application/json" -d '{"username":"additional2test.user@gmail.com","password":"eXoUATest14"}' $host/api/auth/login | sed 's/token//g' | sed 's/[{}:"]//g'`
    curl -X POST -H "Content-Type: application/json" -d '{"name":"additional2test.user"}' $host/api/workspace/create?token=$token

    #deleting test tenants
    echo Stoping tenant: $tenantName7
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName7
    echo Deleting tenant: $tenantName7
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName7

    echo Stoping tenant: $tenantName8
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName8
    echo Deleting tenant: $tenantName8
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName8

	#wait for tenant status ONLINE
	FLAG="1"
	while [ $FLAG == "1" ]; do
	
	status=`curl -u cldadmin:tomcat -X GET -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/tenant-status?tenant=$tenantName5`
	
	if [[ $status == *ONLINE* ]];
	    then
		echo '********************************'
		echo "Tenant $tenantName5 is created!"
		echo '********************************'
		FLAG="0"
		else  sleep 5 ;
	    fi;
done

else

    echo "Recreating tenants for GOOGLE CHROME"
    echo "=================================="
    echo Stoping tenant: $tenantName
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName
    echo Deleting tenant: $tenantName
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName
    echo Creating new tenant: $tenantName
    token=`curl -X POST -H "Content-Type: application/json" -d '{"username":"exoinvitemain@gmail.com","password":"eXoUATest14"}' $host/api/auth/login | sed 's/token//g' | sed 's/[{}:"]//g'`
    curl -X POST -H "Content-Type: application/json" -d '{"name":"exoinvitemain"}' $host/api/workspace/create?token=$token

    echo Stoping tenant: $tenantName2
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName2
    echo Deleting tenant: $tenantName2
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName2
    echo Creating new tenant: $tenantName2
    token=`curl -X POST -H "Content-Type: application/json" -d '{"username":"additional.test.user@gmail.com","password":"eXoUATest14"}' $host/api/auth/login | sed 's/token//g' | sed 's/[{}:"]//g'`
    curl -X POST -H "Content-Type: application/json" -d '{"name":"additional.test.user"}' $host/api/workspace/create?token=$token

    #deleting test tenants
    echo Stoping tenant: $tenantName3
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName3
    echo Deleting tenant: $tenantName3
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName3

    echo Stoping tenant: $tenantName4
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=$tenantName4
    echo Deleting tenant: $tenantName4
    curl -u cldadmin:tomcat  -X POST -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=$tenantName4

	#wait for tenant status ONLINE
	FLAG="1"
	while [ $FLAG == "1" ]; do
	
	status=`curl -u cldadmin:tomcat -X GET -k $host/cloud-admin/shell/rest/private/cloud-admin/tenant-service/tenant-status?tenant=$tenantName`
	
	if [[ $status == *ONLINE* ]];
	    then
		echo '********************************'
		echo "Tenant $tenantName is created!"
		echo '********************************'
		FLAG="0"
		else  sleep 5 ;
        fi;
    done

fi

#kill all chrome processes
if ! kill `ps -A |grep chrome| cut -d "?" -f1` > /dev/null 2>&1; then
    echo "Could not send SIGTERM to process" >&2
fi

echo 'launching tests'
mvn clean integration-test -Pselenium-test $2 $3 $4 $5
