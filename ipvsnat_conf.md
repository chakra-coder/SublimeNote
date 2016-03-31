# ipvs_nat_conf

```
global_defs {
   router_id LVS_MASTER            #BACKUP上修改为LVS_BACKUP
}
vrrp_instance VI_1 {
    state MASTER                   #BACKUP上修改为BACKUP
    interface eth1
    virtual_router_id 51
    priority 100                   #BACKUP上修改为80
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        210.14.137.119
    }
}

vrrp_instance LAN_GATEWAY {
    state MASTER                   #BACKUP上修改为LVS_BACKUP
    interface eth0
    virtual_router_id 52
    priority 100                   #BACKUP上修改为80
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.1.10
    }
}

virtual_server 210.14.137.119 8080 {
    delay_loop 6
    lb_algo rr
    lb_kind NAT
#   persistence_timeout 5
    protocol TCP

    real_server 192.168.1.124 8080 {
        weight 3
        TCP_CHECK {
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
            connect_port 8080
        }
    }
    
    real_server 192.168.1.125 8080 {
        weight 3
        TCP_CHECK {
            connect_timeout 3
            nb_get_retry 3
            delay_before_retry 3
            connect_port 8080
        }
    }
}
```