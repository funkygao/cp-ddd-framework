package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.annotation.Partner;

/**
 * 某个{@link Partner}被kill后，它的扩展点执行时会强制抛出该异常.
 */
class PartnerKilledException extends RuntimeException {

    PartnerKilledException(String code) {
        super("Partner:" + code + " killed");
    }

}
