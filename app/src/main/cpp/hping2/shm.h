#ifndef SHM_H
#define SHM_H

int shm_creat(int size);
void* shm_attach(void);
int shm_rm(void);
int shm_detach(void *addr);
void* shm_init(int size);
void shm_close(void *addr);

#endif /* SHM_H */

