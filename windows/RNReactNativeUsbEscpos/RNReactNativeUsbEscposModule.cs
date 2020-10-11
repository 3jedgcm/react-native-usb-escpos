using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace React.Native.Usb.Escpos.RNReactNativeUsbEscpos
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNReactNativeUsbEscposModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNReactNativeUsbEscposModule"/>.
        /// </summary>
        internal RNReactNativeUsbEscposModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNReactNativeUsbEscpos";
            }
        }
    }
}
