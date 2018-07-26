# $smu-mark$ 
# $name: Makefile.in$ 
# $author: Salvatore Sanfilippo 'antirez'$ 
# $copyright: Copyright (C) 1999 by Salvatore Sanfilippo$ 
# $license: This software is under GPL version 2 of license$ 
# $date: Sun Jul 25 17:56:15 MET DST 1999$ 
# $rev: 3$ 

CC= gcc
AR=/usr/bin/ar
RANLIB=/usr/bin/ranlib
CCOPT= -O2 -Wall 
DEBUG= -g
#uncomment the following if you need libpcap based build under linux
#(not raccomanded)
COMPILE_TIME= 
INSTALL_MANPATH=/usr/local/man


OBJ=	main.o getifname.o getlhs.o \
	linux_sockpacket.o parseoptions.o datafiller.o \
	datahandler.o gethostname.o \
	binding.o getusec.o opensockraw.o \
	logicmp.o waitpacket.o resolve.o \
	sendip.o sendicmp.o sendudp.o \
	sendtcp.o cksum.o statistics.o \
	usage.o version.o antigetopt.o \
	sockopt.o listen.o \
	sendhcmp.o memstr.o rtt.o \
	relid.o sendip_handler.o \
	libpcap_stuff.o memlockall.o memunlockall.o \
	memlock.o memunlock.o ip_opt_build.o \
	display_ipopt.o sendrawip.o signal.o send.o \
	strlcpy.o arsglue.o random.o scan.o \
	hstring.o libars.a

ARSOBJ = ars.o apd.o split.o

all: hping2

libars.a: $(ARSOBJ)
	$(AR) rc $@ $^
	$(RANLIB) $@

hping2: byteorder.h $(OBJ)
	$(CC) -o hping2 $(CCOPT) $(DEBUG) $(OBJ) $(PCAP) 
	@echo
	./hping2 -v
	@echo "use \`make strip' to strip hping2 binary"
	@echo "use \`make install' to install hping2"

byteorder.h:
	./configure

.c.o:
	$(CC) -c $(CCOPT) $(DEBUG) $(COMPILE_TIME) $<

clean:
	rm -rf hping2 *.o *.a
	-(cd utils; $(MAKE) clean)

distclean:
	rm -rf hping2 *.o *.a byteorder byteorder.h systype.h Makefile
	-(cd utils; $(MAKE) clean)

install: hping2
	cp -f hping2 /usr/sbin/
	chmod 755 /usr/sbin/hping2
	ln -sf /usr/sbin/hping2 /usr/sbin/hping
	@if [ -d ${INSTALL_MANPATH}/man8 ]; then \
		cp ./docs/hping2.8 ${INSTALL_MANPATH}/man8; \
		chmod 644 ${INSTALL_MANPATH}/man8/hping2.8; \
	else \
		echo "@@@@@@ WARNING @@@@@@"; \
		echo "Can't install the man page: ${INSTALL_MANPATH}/man8 does not exist"; \
	fi

strip: hping2
	@ls -l ./hping2
	strip hping2
	@ls -l ./hping2
