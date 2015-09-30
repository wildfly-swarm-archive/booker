gnome-terminal \
	--tab \
		 --working-directory ${HOME}/apps/keycloak-1.5.0.Final \
		-e ${HOME}/bin/run_keycloak.sh
sleep 10
gnome-terminal \
	--tab \
		--working-directory ${HOME}/apps/logstash-1.5.4 \
		-e ${HOME}/bin/run_logstash.sh
sleep 10
gnome-terminal \
	--tab \
		 --working-directory ${HOME}/booker/web-client \
		-e ${HOME}/bin/run_webclient.sh
sleep 10
gnome-terminal \
	--tab \
		--working-directory ${HOME}/booker/store \
		-e ${HOME}/bin/run_store.sh
sleep 5
gnome-terminal \
	--tab \
		--working-directory ${HOME}/booker/pricing \
		-e ${HOME}/bin/run_pricing.sh
sleep 5
gnome-terminal \
	--tab \
		--working-directory ${HOME}/booker/library \
		-e ${HOME}/bin/run_library.sh

gnome-terminal \
	--tab \
		--working-directory ${HOME}/apps/kibana-4.1.2-linux-x64 \
		-e ${HOME}/bin/run_kibana.sh

