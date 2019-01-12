//
// Created by maciu on 07/01/19.
//
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <signal.h>
#include <time.h>
#include <jni.h>
#include <android/log.h>

#include "hping2.h"

/* globals */
unsigned int
        tcp_th_flags = 0,
        linkhdr_size,				/* physical layer header size */
        ip_tos = 0,
        set_seqnum = FALSE,
        tcp_seqnum = FALSE,
        set_ack,
        h_if_mtu,
        virtual_mtu	= DEFAULT_VIRTUAL_MTU,
        ip_frag_offset	= 0,
        signlen,
        lsr_length = 0,
        ssr_length = 0,
        tcp_ack;


unsigned short int
        data_size = 0;

float
        rtt_min = 0,
        rtt_max = 0,
        rtt_avg = 0;

int
        sockpacket,
        sockraw,
        sent_pkt = 0,
        recv_pkt = 0,
        out_of_sequence_pkt = 0,
        sending_wait = DEFAULT_SENDINGWAIT,	/* see DEFAULT_SENDINGWAIT */
        opt_rawipmode	= FALSE,
        opt_icmpmode	= FALSE,
        opt_udpmode	= FALSE,
        opt_scanmode	= FALSE,
        opt_listenmode  = FALSE,
        opt_waitinusec	= FALSE,
        opt_numeric	= FALSE,
        opt_gethost	= TRUE,
        opt_quiet	= FALSE,
        opt_relid	= FALSE,
        opt_fragment	= FALSE,
        opt_df		= FALSE,
        opt_mf		= FALSE,
        opt_debug	= FALSE,
        opt_verbose	= FALSE,
        opt_winid_order = FALSE,
        opt_keepstill	= FALSE,
        opt_datafromfile= FALSE,
        opt_hexdump	= FALSE,
        opt_contdump	= FALSE,
        opt_sign	= FALSE,
        opt_safe	= FALSE,
        opt_end		= FALSE,
        opt_traceroute  = FALSE,
        opt_seqnum	= FALSE,
        opt_incdport	= FALSE,
        opt_force_incdport = FALSE,
        opt_icmptype	= DEFAULT_ICMP_TYPE,
        opt_icmpcode	= DEFAULT_ICMP_CODE,
        opt_rroute	= FALSE,
        opt_tcpexitcode	= FALSE,
        opt_badcksum	= FALSE,
        opt_tr_keep_ttl = FALSE,
        opt_tcp_timestamp = FALSE,
        opt_tr_stop	= FALSE,
        opt_tr_no_rtt	= FALSE,
        opt_rand_dest	= FALSE,
        opt_rand_source	= FALSE,
        opt_lsrr        = FALSE,
        opt_ssrr        = FALSE,
        opt_cplt_rte    = FALSE,
        tcp_exitcode	= 0,
        src_ttl		= DEFAULT_TTL,
        src_id		= -1, /* random */
        base_dst_port	= DEFAULT_DPORT,
        dst_port	= DEFAULT_DPORT,
        src_port,
        sequence	= 0,
        initsport	= DEFAULT_INITSPORT,
        src_winsize	= DEFAULT_SRCWINSIZE,
        src_thoff 	= (TCPHDR_SIZE >> 2),
        count		= DEFAULT_COUNT,
        ctrlzbind	= DEFAULT_BIND,
        delaytable_index= 0,
        eof_reached	= FALSE,
        icmp_ip_version = DEFAULT_ICMP_IP_VERSION,
        icmp_ip_ihl	= DEFAULT_ICMP_IP_IHL,
        icmp_ip_tos	= DEFAULT_ICMP_IP_TOS,
        icmp_ip_tot_len = DEFAULT_ICMP_IP_TOT_LEN,
        icmp_ip_id	= DEFAULT_ICMP_IP_ID,
        icmp_ip_protocol= DEFAULT_ICMP_IP_PROTOCOL,
        icmp_ip_srcport	= DEFAULT_DPORT,
        icmp_ip_dstport	= DEFAULT_DPORT,
        opt_force_icmp  = FALSE,
        icmp_cksum	= DEFAULT_ICMP_CKSUM,
        raw_ip_protocol	= DEFAULT_RAW_IP_PROTOCOL;

char
        datafilename	[1024],
        targetname	[1024],
        targetstraddr	[1024],
        ifname		[1024] = {'\0'},
        ifstraddr	[1024],
        spoofaddr	[1024],
        icmp_ip_srcip	[1024],
        icmp_ip_dstip	[1024],
        icmp_gwip	[1024],
        sign		[1024],
        rsign		[1024], /* reverse sign (hping -> gniph) */
        ip_opt		[40],
        *opt_scanports = "";

unsigned char
        lsr		[255] = {0},
        ssr		[255] = {0};

unsigned
        ip_optlen	= 0;

struct sockaddr_in
        icmp_ip_src,
        icmp_ip_dst,
        icmp_gw,
        local,
        remote;

struct itimerval usec_delay;
volatile struct delaytable_element delaytable[TABLESIZE];

struct hcmphdr *hcmphdr_p; /* global pointer used by send_hcmp to transfer
			      hcmp headers to data_handler */


/*
extern "C" JNIEXPORT void JNICALL Java_com_root_hping2_SettingsScreenActivity_callMain(JNIEnv *env, jobject object, jobject stringArray){
    // your argc
    int size = (*env)->GetArrayLength(env, stringArray);
    char **argv = new char*[size];
    for (int i=0; i < size; ++i)
    {
        jstring string = (*env)->GetObjectArrayElement(stringArrays, i);
        const char* cString = (*env)->GetStringUTFChars(string, 0);
        argv[i] = strdup(cString);
        (*env)->ReleaseStringUTFChars(string, myarray);
        (*env)->DeleteLocalRef(string);
    }
    // you will need to modify main so it does properly cleanup
    main(argc, argv);
    // clean up
    for(int i = 0; i < size; ++i)
        free(argv[i]);
    delete [] argv;
}*/

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    __android_log_print(ANDROID_LOG_INFO,  __FUNCTION__, "onLoad");
    JNIEnv* env;
    if ((*vm)->GetEnv(vm, (void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL Java_com_root_hping2_main_callMainFromJNI(JNIEnv *env, jobject pThis, jstring argument){

    char buf[128];
    const char *str = (*env)->GetStringUTFChars(env, argument, 0);
    printf("%s", str);
    (*env)->ReleaseStringUTFChars(env, argument, str);

}



int main(int argc, char **argv) {
    char setflags[1024] = {'\0'};
    int c, hdr_size;


}