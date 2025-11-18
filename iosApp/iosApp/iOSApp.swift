import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        KoinHelper.Companion.shared.ensureKoinStarted()
    }

    @State private var showSplash = true

        var body: some Scene {
            WindowGroup {
                ZStack {
                    ContentView()
                        .opacity(showSplash ? 0 : 1)
                        .scaleEffect(showSplash ? 1.1 : 1)
                        .offset(y: showSplash ? 40 : 0)
                        .animation(.easeOut(duration: 0.6), value: showSplash)

                    if showSplash {
                        SplashScreenView()
                            .transition(.opacity)
                            .zIndex(1)
                    }
                }
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                        withAnimation(.easeOut(duration: 0.6)) {
                            showSplash = false
                        }
                    }
                }
            }
        }
}

