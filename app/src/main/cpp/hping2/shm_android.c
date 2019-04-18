#include <stddef.h>
#include <stdio.h>
#include <sys/mman.h>
#include "shm.h"

#ifdef __cplusplus
extern "C" {
#endif
int ashmem_create_region(const char *name, size_t size);
int ashmem_set_prot_region(int fd, int prot);
int ashmem_pin_region(int fd, size_t offset, size_t len);
int ashmem_unpin_region(int fd, size_t offset, size_t len);
int ashmem_get_size_region(int fd);
#ifdef __cplusplus
}
#endif
#ifndef __ASHMEMIOC	/* in case someone included <linux/ashmem.h> too */
#define ASHMEM_NAME_LEN		256
#define ASHMEM_NAME_DEF		"dev/ashmem"
/* Return values from ASHMEM_PIN: Was the mapping purged while unpinned? */
#define ASHMEM_NOT_PURGED	0
#define ASHMEM_WAS_PURGED	1
/* Return values from ASHMEM_UNPIN: Is the mapping now pinned or unpinned? */
#define ASHMEM_IS_UNPINNED	0
#define ASHMEM_IS_PINNED	1
#endif	/* ! __ASHMEMIOC */

char const* shmname = "hping2_shared_mem";
static int id;	/* shared memory id */

int shm_creat(int size)
{
  id = ashmem_create_region("my_shm_region", (size_t)size);
	if (id == -1)
	{
		perror("[shm_creat] shm_open");
		return -1; /* on error -1 */
	}
	return id; /* on success > 0 */
}

void *shm_attach(void)
{
  size_t size = ashmem_get_size_region(id);
	void *shared;

  shared = mmap(NULL, (size_t)size, PROT_READ | PROT_WRITE, MAP_PRIVATE, id, 0);
	if (shared == MAP_FAILED)
	{
		perror("[shm_attach] mmap");
		return NULL; /* on error NULL */
	}
	return shared; /* on success the address */

}

int shm_rm(void)
{
	return 0; /* ashmem_pin_region and ashmem_unpin_region apparently do nothing, so no worries here */
}

int shm_detach(void *addr)
{
  size_t size = ashmem_get_size_region(id);
  return munmap(addr, size);
}

void *shm_init(int size)
{
  if (shm_creat(size) == -1)
    return NULL;
  return shm_attach();
}

void shm_close(void *addr)
{
  shm_detach(addr);
  shm_rm();
}

